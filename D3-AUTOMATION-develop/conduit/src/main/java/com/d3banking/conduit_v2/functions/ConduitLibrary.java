package com.d3banking.conduit_v2.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.xml.sax.SAXException;

public class ConduitLibrary extends DbConnection{

	
	
	
	
	/**
	 * Conduit folder path where files get processed
	 */
	public String folderPathWhereDropFile() throws ParserConfigurationException, SAXException, IOException {
		String pathToDropFile = null;
		pathToDropFile = getTestDataFor("conduitDirPath").get(0);
		return pathToDropFile;
	}

	/**
	 * Drop the conduit test file in the incomingData folder
	 */
	public void dropConduitTestFile(
			String sourceFile, 
			String destinationPath) 
			{
				try {
					String sourcePath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "") + "/" + sourceFile;
					String tempPath = getClass().getResource("/" + conduitDir).toString().replace("file:/", "") + "/temp_" + sourceFile;
					File source = new File(sourcePath);
					File tempDest = new File(tempPath);
					InputStream in = new FileInputStream(source);
					File dest = new File(destinationPath, source.getName());
					OutputStream out = new FileOutputStream(tempDest);
					byte[] buf = new byte[10000];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
					tempDest.renameTo(dest);
					System.out.println("Conduit file copied to \""+ dest.getCanonicalPath() + "\".");
				} catch (FileNotFoundException ex) {
					System.out.println(ex.getMessage() + " in the specified directory.");
					System.exit(0);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}

	/**
	 * Wait until Conduit test file processing finishes
	 * 
	 * @throws InterruptedException
	 */
	public void waitConduitFileProcessing(
			String sourceFile,
			String destinationPath, 
			int waitSecs) throws InterruptedException 
			{
				File file = new File(destinationPath, sourceFile);
				Calendar now = Calendar.getInstance();
				int secs = now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND);
				int secsToWait = secs + waitSecs;
				while (secsToWait > secs) {
					if (!file.exists()) {
						break;
					}
					now = Calendar.getInstance();
					secs = now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND);
				}
				if (file.exists()) {
					int attempt = 0;
					while (file.exists()) {
						file.delete();
						if (attempt > 3) {
							break;
						}
					}
					Assert.fail("Waited "
							+ sourceFile
							+ " file processing for "
							+ waitSecs
							+ " secs. The file is not moved into processed folder. Deleting file & existing test!");
				}
			}

	/**
	 * Validate Conduit processing creates a file
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws InterruptedException
	 */
	public void validateFileStoredIn(String folderName) throws ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		String _folderPath = folderPathWhereDropFile() + "\\" + folderName;
		File _filesInFolder = new File(_folderPath);
		File[] files = _filesInFolder.listFiles();
		int _waitTime = 0;
		while (files.length == 0) {
			Thread.sleep(1000);
			_waitTime = _waitTime + 1;
			files = _filesInFolder.listFiles();
			if (_waitTime > 10) {
				break;
			}
		}
		Assert.assertTrue("No file has been created in the  folder, "+ folderName + " after Conduit file process!",files.length > 0);
		//Assert.assertTrue("More than one file has been created in the  folder, "+ folderName + " after Conduit file process!" ,files.length == 1);
	}

	/**
	 * Validate Conduit processing does not create any file
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws InterruptedException
	 */
	public void validateFileNotStoredIn(String folderName) throws ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		String _folderPath = folderPathWhereDropFile() + "\\" + folderName;
		File _filesInFolder = new File(_folderPath);
		File[] files = _filesInFolder.listFiles();
		if (files.length > 0) {
			Thread.sleep(1000);
			files = _filesInFolder.listFiles();
			if (files.length > 0) {
				Thread.sleep(2000);
				files = _filesInFolder.listFiles();
				if (files.length > 0) {
					Thread.sleep(5000);
					files = _filesInFolder.listFiles();
					if (files.length > 0) {
						Thread.sleep(10000);
						files = _filesInFolder.listFiles();
					}
				}
			}
		}
		Assert.assertTrue("The file has been created in the  folder ("+ folderName + ") after Conduit file process!",files.length == 0);
	}

	/**
	 * Delete existing files
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws InterruptedException
	 */
	public void deleteTrackedFiles(String folderName) throws ParserConfigurationException, SAXException, IOException,InterruptedException 
	{
		String _folderPath = folderPathWhereDropFile() + "\\" + folderName;
		File _filesInFolder = new File(_folderPath);
		File[] files = _filesInFolder.listFiles();
		while (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			files = _filesInFolder.listFiles();
		}
	}

	/**
	 * Delete all Conduit files
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public void deleteConduitFiles() throws ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		deleteTrackedFiles(proccessedDir);
		deleteTrackedFiles(errorDir);
		deleteTrackedFiles(failedDir);
		deleteTrackedFiles(duplicateDir);
	}

	/**
	 * Validate system error message
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public void validateSystemEventErrMsg(
	String _expectedStringInTheEventMsg)throws NumberFormatException, ClassNotFoundException, SQLException,ParserConfigurationException, SAXException, IOException,InterruptedException 
	{
		List<Object> stmt = connectToDB();
		int _totalEvent = Integer.parseInt(runQuery(stmt,
				"SELECT COUNT(*) FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, true));
		String _eventDesc = runQuery(stmt,
				"SELECT description FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, true);
		Assert.assertTrue("No system even has been created in the DB.",
				(_totalEvent > 0));
		Assert.assertTrue(
				"More than one system evens have been created in the DB.",
				(_totalEvent == 1));
		try {Assert.assertTrue(
				"The system error message content is not right. Expected string in the exception detail: ("
						+ _expectedStringInTheEventMsg + ").",
				_eventDesc.toLowerCase().contains(_expectedStringInTheEventMsg.toLowerCase()));}
		catch(AssertionError e)
		{
			_eventDesc = runQuery(stmt,
					"SELECT exception_detail FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, false);
			
			Assert.assertTrue(
					"The system error message content is not right. Expected string in the exception detail: ("
							+ _expectedStringInTheEventMsg + "). Actual: " + _eventDesc.toLowerCase(),
					_eventDesc.toLowerCase().contains(_expectedStringInTheEventMsg.toLowerCase()));
		}
	}
	
	/**
	 * Delete existing test data
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void deleteAllTestData() throws ParserConfigurationException,SAXException, IOException, ClassNotFoundException, SQLException,InterruptedException 
	{
		List<Object> stmt = connectToDB();
		
		
		String query = "delete from [system_event]\r\n" + 
				"delete from [conduit_stats]\r\n" + 
				"delete from [migration_lookup]";
		try {
			runQuery(stmt, query, true, true);
		} catch (Exception e) {
			System.out.println(e);
		}
		closeDBConnection(stmt);
	}
	
	/**
	 * Validates that no system event has been created in the DB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void validateNoSystemEventCreated() throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		int _totalEvent = Integer.parseInt(runQuery(stmt,
				"SELECT COUNT(*) FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, true));
		Assert.assertTrue("System even has been created in the DB! SYSTEM EVENT:" + runQuery(stmt,
				"  SELECT exception_detail FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, true),(_totalEvent == 0));
		closeDBConnection(stmt);
	}
	
	/**
	 * Validates that no system event has been created in the DB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void validateNumOfSystemEvents(int numOfSystemEvents, String expectedStringInTheEventMsg) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		int totalEvent = Integer.parseInt(runQuery(stmt,
				"SELECT COUNT(*) FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, true));
		String _eventDesc = runQuery(stmt,
				"SELECT description FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, true);
		Assert.assertTrue(
				"Total number of events is not right. Expected: " + numOfSystemEvents + " Actual:" + totalEvent,
				(totalEvent == numOfSystemEvents));
		try {Assert.assertTrue(
				"The system error message content is not right. Expected string in the exception detail: ("
						+ expectedStringInTheEventMsg + ").",
				_eventDesc.toLowerCase().contains(expectedStringInTheEventMsg.toLowerCase()));}
		catch(AssertionError e)
		{
			_eventDesc = runQuery(stmt,
					"SELECT exception_detail FROM system_event where description NOT LIKE 'Started file processing%' AND  description NOT LIKE 'Ended file processing%'", false, false);
			
			Assert.assertTrue(
					"The system error message content is not right. Expected string in the exception detail: ("
							+ expectedStringInTheEventMsg + "). Actual: " + _eventDesc.toLowerCase(),
					_eventDesc.toLowerCase().contains(expectedStringInTheEventMsg.toLowerCase()));
		}
	}
	
	/**
	 * Compare data in the xml file with what is in the DB after Conduit file processing
	 * @param _stmt
	 * @param _sqlQuery
	 * @param _fileName
	 * @param _index
	 * @param _fieldName
	 * @param _parentElement
	 * @param _childElement
	 * @param _attribute
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void compareDataInXMLWithDB (
			List<Object> stmt, 
			String sqlQuery, 
			String conduitFile, 
			String fieldToCheck, 
			String valueInXML) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
			{
				ResultSet rs=null;
				Statement _stmt = (Statement) stmt.get(0);
			  	rs = ((Statement) _stmt).executeQuery(sqlQuery);
			  	rs.next();
			  	try{
				  	if(valueInXML.equals("elementDoesNotExist"))
				  	{
				  		try
				  		{
				  			if(rs.getString(1).equals(""))
				  			{
				  				valueInXML="";
				  				System.out.println(fieldToCheck + ": - IN XML:" + valueInXML + "  IN DB:" +   rs.getString(1));
				  		  		Assert.assertEquals(fieldToCheck + " is incorrect!",valueInXML,  rs.getString(1));
				  			}	
				  		}
				  		catch(NullPointerException e)
				  		{
				  			System.out.println(fieldToCheck + ": - IN XML:" + valueInXML + "  IN DB: NULL");
				  		}
				  		catch(SQLException e)
				  		{
				  			if (!e.getMessage().equals("No current row in the ResultSet."))
				  			{
				  				Assert.fail(e.getMessage());
				  			}
				  		}
				  	}else{
				  		try
				  		{
				  			System.out.println(fieldToCheck + ": - IN XML:" + valueInXML + "  IN DB:" +   rs.getString(1));
				  			if (valueInXML.toLowerCase().equals("datetime")){
				  				Assert.assertEquals(fieldToCheck + " is not stored in the db or not matching what is in the xml file!","date_time",  rs.getString(1).toLowerCase());
				  			}else{
				  				Assert.assertEquals(fieldToCheck + " is not stored in the db or not matching what is in the xml file!",valueInXML.toLowerCase(),  rs.getString(1).toLowerCase());
				  			}
				  		}
				  		catch (SQLException e)
				  		{
				  			Assert.fail("Could not find " + valueInXML + " value for field " + fieldToCheck + ".");
				  		}
				  		
				  	}	  		
			  	}
			  	catch(IndexOutOfBoundsException e){}
			}
	/**
	 * Validate companyList element values stored in the DB
	 * @param conduitFile
	 * @param fileProcessingShouldFail
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateCompanyListElement (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
		
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck, valueInXML;	
		List<String> companyUid = getElementValueInConduitXMLFile(conduitFile, "comp", "", "uid");
		for (int u = 0; u <companyUid.size(); u++)
		{
			//validate uid
			elementToCheck = "Company UID";
			query = "select source_company_id from company where source_company_id = '"+companyUid.get(u)+"'";
			List<String> valuesInXML = getElementValueInConduitXMLFile(conduitFile, "comp", "", "uid");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate puid
			elementToCheck = "Company PUID";
			query = "select source_company_id from company where source_company_id = '"+companyUid.get(u)+"' and\r\n" + 
					"parent_id = (select business_id from company where source_company_id = '"+getElementValueInConduitXMLFile(conduitFile, "comp", "", "puid").get(u)+"')";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "comp", "", "uid");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate tp
			elementToCheck = "tp";
			query = "select structure from company where source_company_id = '"+companyUid.get(u)+"'";
			List<String> acctProdTPs = getElementValueInConduitXMLFile(conduitFile, "acctprod", "tp", "");
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "comp", "tp", "");
			List<String> compTps = new ArrayList<String>();
			String acctProdTpsinXML = null;
			for(int z=0; z<acctProdTPs.size(); z++)
			{
				acctProdTpsinXML = acctProdTpsinXML + "|"+ acctProdTPs.get(z);
			}
			for(int x=0; x<valuesInXML.size(); x++)
			{
				if (!acctProdTpsinXML.contains(valuesInXML.get(x)))
				{
					compTps.add(valuesInXML.get(x));
				}
			}
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  compTps.get(u));
			//validate business/name
			elementToCheck = "Business/Name";
			query = "select t2.name from company as t1, [business] as t2 where t1.business_id=t2.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "nm", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/phone
			elementToCheck = "Business/Phone";
			query = "select t2.phone from company as t1, [business] as t2 where t1.business_id=t2.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal address1
			elementToCheck = "Business/Physcal Address1";
			query = "select t3.address1 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a1", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal address2
			elementToCheck = "Business/Physcal Address2";
			query = "select t3.address2 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a2", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal address3
			elementToCheck = "Business/Physcal Address3";
			query = "select t3.address3 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a3", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal address4
			elementToCheck = "Business/Physcal Address4";
			query = "select t3.address4 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a4", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal city
			elementToCheck = "Business/Physcal City";
			query = "select t3.city from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "ct", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal state
			elementToCheck = "Business/Physcal State";
			query = "select t3.state from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "st", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal country
			elementToCheck = "Business/Physcal Country";
			query = "select t3.country_code from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "cc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal postal code
			elementToCheck = "Business/Physcal PostalCode";
			query = "select t3.postal_code from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "pc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal latitude
			elementToCheck = "Business/Physcal Latitude";
			query = "select t3.latitude from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "lt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/physcal longitude
			elementToCheck = "Business/Physcal Longitude";
			query = "select t3.longitude from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.physical_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "ln", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing address1
			elementToCheck = "Business/Mailing Address1";
			query = "select t3.address1 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a1", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing address2
			elementToCheck = "Business/Mailing Address2";
			query = "select t3.address2 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a2", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing address3
			elementToCheck = "Business/Mailing Address3";
			query = "select t3.address3 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a3", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing address4
			elementToCheck = "Business/Mailing Address4";
			query = "select t3.address4 from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a4", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing city
			elementToCheck = "Business/Mailing Cit";
			query = "select t3.city from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "ct", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing state
			elementToCheck = "Business/Mailing State";
			query = "select t3.state from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "st", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing country
			elementToCheck = "Business/Mailing Country";
			query = "select t3.country_code from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "cc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing postal code
			elementToCheck = "Business/Mailing PostalCode";
			query = "select t3.postal_code from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "pc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing latitude
			elementToCheck = "Business/Mailing Latitude";
			query = "select t3.latitude from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "lt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/mailing longitude
			elementToCheck = "Business/Mailing Longitude";
			query = "select t3.longitude from company as t1, [business] as t2, [address] as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.mailing_address_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "ln", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact1/first Name
			elementToCheck = "Contact1/FirstName";
			query = "select t3.first_name from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact1_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct1", "fn", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact1/middle name
			elementToCheck = "Contact1/MiddleName";
			query = "select t3.[middle_name] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact1_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct1", "mn", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact1/last name
			elementToCheck = "Contact1/LastName";
			query = "select t3.[last_name] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact1_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct1", "ln", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact1/primary email
			//validate business/Contact1/ alternate email
			//validate business/Contact1/home phone
			elementToCheck = "Contact1/HomePhone";
			query = "select t3.[home_phone] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact1_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct1", "hph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u).replace("-", "").replace(" ", ""));
			//validate business/Contact1/work phone
			elementToCheck = "Contact1/WorkPhone";
			query = "select t3.[work_phone] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact1_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct1", "wph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact1/mobile phone
			elementToCheck = "Contact1/MobilePhone";
			query = "select t3.[mobile_phone] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact1_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct1", "mph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact1/ [employee]
			elementToCheck = "Contact1/Employee";
			query = "select t3.employee from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact1_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct1", "emp", "");
			if (valuesInXML.get(u).equals("true"))
			{
				valueInXML = "1";
				
			}else{
				valueInXML = "0";
			}
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
			//validate business/Contact2/first Name
			elementToCheck = "Contact2/FirstName";
			query = "select t3.first_name from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact2_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct2", "fn", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact2/middle name
			elementToCheck = "Contact2/MiddleName";
			query = "select t3.[middle_name] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact2_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct2", "mn", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact2/last name
			elementToCheck = "Contact2/LastName";
			query = "select t3.[last_name] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact2_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct2", "ln", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact2/primary email
			//validate business/Contact2/ alternate email
			//validate business/Contact2/home phone
			elementToCheck = "Contact2/HomePhone";
			query = "select t3.[home_phone] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact2_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct2", "hph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u).replace("-", "").replace(" ", ""));
			//validate business/Contact2/work phone
			elementToCheck = "Contact2/WorkPhone";
			query = "select t3.[work_phone] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact2_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct2", "wph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact2/mobile phone
			elementToCheck = "Contact2/Mobile";
			query = "select t3.[mobile_phone] from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact2_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct2", "mph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate business/Contact2/ [employee]
			elementToCheck = "Contact2/Employee";
			query = "select t3.employee from company as t1, [business] as t2, person as t3 \r\n" + 
					"where t1.business_id=t2.id and t2.contact2_id=t3.id and t1.source_company_id = '"+companyUid.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "ct2", "emp", "");
			if (!valuesInXML.get(u).equals("elementDoesNotExist"))
			{
				if (valuesInXML.get(u).equals("true"))
				{
					valueInXML = "1";
					
				}else if(valuesInXML.get(u).equals("false")){
					valueInXML = "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
			}
			//validate acctprodlst
			List<String> acctProdUids = getElementValueInConduitXMLFile(conduitFile, "acctprod", "", "uid");
			for (int a = 0; a <acctProdUids.size(); a++)
			{
				if (!acctProdUids.get(a).equals("elementDoesNotExist"))
				{
					//validate acctprodlst/uid
					elementToCheck = "AccountProd/UID";
					query = "select source_product_id from [account_product] where source_product_id = '"+acctProdUids.get(a)+"'";
					valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctprod", "", "uid");
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
					//validate acctprodlst/puid
					elementToCheck = "AccountProd/PUID";
					String puid = getElementValueInConduitXMLFile(conduitFile, "acctprod", "", "puid").get(a);
					query = "select source_product_id from [account_product] where source_product_id = '"+acctProdUids.get(a)+"' and\r\n" + 
							"parent_id = (select id from [account_product] where source_product_id = '"+puid+"')";
					if (!puid.equals("elementDoesNotExist"))
					{
						compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
					}
					//validate acctprodlst/ac
					elementToCheck = "Account/Prod/Accounting Classification";
					query = "select accounting_class from account_product where source_product_id = '"+acctProdUids.get(a)+"'";
					valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctprod", "ac", "");
					if (valuesInXML.get(a).equals("l"))
					{
						valueInXML = "LIABILITY";
					}else if (valuesInXML.get(a).equals("a"))
					{
						valueInXML = "ASSET";
					}else{
						valueInXML = valuesInXML.get(a).trim().toUpperCase();
					}
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
					//validate acctprodlst/name
					elementToCheck = "Account/Prod/Name";
					query = "select name from [account_product] where source_product_id = '"+acctProdUids.get(a)+"'";
					valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctprod", "nm", "");
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
				}
			}				
			//validate acctprodlst/attributes
			List<String> acctProdAtts = getElementValueInConduitXMLFile(conduitFile, "acctprod", "alst", "a", "", "n");
			List<String> compUID = getElementValueInConduitXMLFile(conduitFile, "comp", "", "uid");
			for (int c = 0; c <compUID.size(); c++)
			{
				if (!compUID.get(c).equals("elementDoesNotExist"))
				{
					for (int a = 0; a <acctProdAtts.size(); a++)
					{
						if (!acctProdAtts.get(a).equals("elementDoesNotExist"))
						{
							//validate acctprodlst/alst/name
							elementToCheck = "AccountProduct Attribute" + (a+1) + ": name";
							query = "select t1.name from account_product_attribute as t1, account_product as t2, company as t3\r\n" + 
									"where t1.account_product_id=t2.id and t2.company_id=t3.id and t3.source_company_id='"+compUID.get(c)+"'\r\n" + 
									"and t1.name='"+acctProdAtts.get(a)+"'";
							valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctprod", "alst", "a", "", "n");
							compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
							//validate acctprodlst/alst/type
							elementToCheck = "AccountProduct Attribute" + (a+1) + ": type";
							query = "select t1.type from account_product_attribute as t1, account_product as t2, company as t3\r\n" + 
									"where t1.account_product_id=t2.id and t2.company_id=t3.id and t3.source_company_id='"+compUID.get(c)+"'\r\n" + 
									"and t1.name='"+acctProdAtts.get(a)+"'";
							valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctprod", "alst", "a", "", "t");
							compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
							//validate acctprodlst/alst/d
							elementToCheck = "AccountProduct Attribute" + (a+1) + ": displayOrder";
							query = "select t1.display_order from account_product_attribute as t1, account_product as t2, company as t3\r\n" + 
									"where t1.account_product_id=t2.id and t2.company_id=t3.id and t3.source_company_id='"+compUID.get(c)+"'\r\n" + 
									"and t1.name='"+acctProdAtts.get(a)+"'";
							valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctprod", "alst", "a", "", "d");
							compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
							//validate acctprodlst/alst/v
							elementToCheck = "AccountProduct Attribute" + (a+1) + ": visible";
							query = "select t1.is_displayable from account_product_attribute as t1, account_product as t2, company as t3\r\n" + 
									"where t1.account_product_id=t2.id and t2.company_id=t3.id and t3.source_company_id='"+compUID.get(c)+"'\r\n" + 
									"and t1.name='"+acctProdAtts.get(a)+"'";
							valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctprod", "alst", "a", "", "v");
							if(valuesInXML.get(a).equals("false"))
							{
								valueInXML = "0";
							}else if(valuesInXML.get(a).equals("true"))
							{
								valueInXML = "1";
							}else{
								valueInXML = valuesInXML.get(a).trim();;
							}
							compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
						}
					}
				}
			}
			//validate company attributes alst
			List<String> compAttn = new ArrayList<String>();
			List<String> compAttt = new ArrayList<String>();
			List<String> compAttd = new ArrayList<String>();
			List<String> allAttn = getElementValueInConduitXMLFile(conduitFile, "comp", "alst", "a", "", "n");
			List<String> allAttt = getElementValueInConduitXMLFile(conduitFile, "comp", "alst", "a", "", "t");
			List<String> allAttd = getElementValueInConduitXMLFile(conduitFile, "comp", "alst", "a", "", "d");
			for (int a = 0; a <allAttn.size(); a++)
			{
				if (allAttn.get(a).toLowerCase().contains("testproduct"))
				{
					compAttn.add(allAttn.get(a));
					compAttt.add(allAttt.get(a));
					compAttd.add(allAttd.get(a));
				}
			}
			for (int a = 0; a <compAttn.size(); a++)
			{
				//validate alst/name
				elementToCheck = "Company Attribute" + (a+1) + ": name";
				query = "select name from [company_attribute] where name = '"+compAttn.get(a)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  compAttn.get(a));
				//validate alst/type
				elementToCheck = "Company Attribute" + (a+1) + ": type";
				query = "select type from [company_attribute] where name = '"+compAttn.get(a)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  compAttt.get(a));
				//validate alst/displayOrder
				elementToCheck = "Company Attribute" + (a+1) + ": displayOrder";
				query = "select display_order from [company_attribute] where name = '"+compAttn.get(a)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  compAttd.get(a));
			}
		}

		closeDBConnection(_stmt);
	}
	
	/**
	 * Check number of companies, businesses and account products in the DB
	 * @param conduitFile
	 * @param expectedNumOfCompanyinDB
	 * @param expectedNumOfAccProdinDB
	 * @param expectedNumOfBusinDB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkNumOfBusProdComp(
			String conduitFile, 
			int expectedNumOfCompanyinDB, 
			int expectedNumOfAccProdinDB, 
			int expectedNumOfBusinDB) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				List<Object> stmt = connectToDB();
				List<String> compUIDs = getElementValueInConduitXMLFile(conduitFile, "comp", "", "uid");
				List<String> accProdUIDs = getElementValueInConduitXMLFile(conduitFile, "acctprod", "", "uid");
				List<String> busNames = getElementValueInConduitXMLFile(conduitFile, "busn", "nm", "");
				int numofExistRecord = 0;
				String str = "";
				for (int n=0; n<compUIDs.size(); n++)
				{
					int total = Integer.parseInt(runQuery(stmt,"select count(*) from company where source_company_id = '"+compUIDs.get(n)+"'", false, true));
					numofExistRecord = total + numofExistRecord;
				}
				Assert.assertEquals("Expected total number of companies in the DB is incorrect!", expectedNumOfCompanyinDB, numofExistRecord);
				numofExistRecord = 0;
				for (int c=0; c<compUIDs.size(); c++)
				{
					for (int n=0; n<accProdUIDs.size(); n++)
					{
						if (accProdUIDs.get(n).length()==0)
						{
							str = "none";
						}else{
							str = accProdUIDs.get(n);
						}
						int total = Integer.parseInt(runQuery(stmt,"select count(*) from account_product as t1, company as t2\r\n" + 
								"where t1.company_id=t2.id and t1.source_product_id= '"+str+"' and t2.source_company_id='"+compUIDs.get(c)+"'", false, true));
						numofExistRecord = total + numofExistRecord;
					}
				}
				Assert.assertEquals("Expected total number of accountProducts in the DB is incorrect!", expectedNumOfAccProdinDB, numofExistRecord);
				numofExistRecord = 0;
				for (int n=0; n<busNames.size(); n++)
				{
					int total = Integer.parseInt(runQuery(stmt,"select count(*) from business where name = '"+busNames.get(n)+"'", false, true));
					numofExistRecord = total + numofExistRecord;
				}
				Assert.assertEquals("Expected total number of businesses in the DB is incorrect!", expectedNumOfBusinDB, numofExistRecord);
				closeDBConnection(stmt);
			}
	
	/**
	 * Check number of accounts in the DB
	 * @param conduitFile
	 * @param expectedNumOfAcctInDB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkNumOfAcct(
			String conduitFile, 
			int expectedNumOfAcctInDB) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				List<Object> stmt = connectToDB();
				List<String> acctUIDs = getElementValueInConduitXMLFile(conduitFile, "acct", "", "uid");
				int numofExistRecord = 0;
				for (int n=0; n<acctUIDs.size(); n++)
				{
					int total = Integer.parseInt(runQuery(stmt,"select count(*) from d3_account where source_account_id = '"+acctUIDs.get(n)+"'", false, true));
					numofExistRecord = total + numofExistRecord;
				}
				Assert.assertEquals("Expected total number of accounts in the DB is incorrect!", expectedNumOfAcctInDB, numofExistRecord);
				closeDBConnection(stmt);
			}
	
	/**
	 * Compare number of transactions in the db
	 * @param conduitFile
	 * @param expectedNumOfTxnInDB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkNumOfTxn(
			String conduitFile, 
			int expectedNumOfTxnInDB) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				List<Object> stmt = connectToDB();
				List<String> acctUIDs = getElementValueInConduitXMLFile(conduitFile, "acct", "", "uid");
				List<String> txnUIDs = getElementValueInConduitXMLFile(conduitFile, "txn", "", "uid");
				int numofExistRecord = 0;
				for (int l=0; l<acctUIDs.size(); l++)
				{
					for (int n=0; n<txnUIDs.size(); n++)
					{
						int total = Integer.parseInt(runQuery(stmt,"select count(t1.source_tx_id) from [d3_transaction] as t1, [d3_account] as t2\r\n" + 
								"where t1.account_id=t2.id and t2.source_account_id='"+acctUIDs.get(l)+"' and t1.source_tx_id='"+txnUIDs.get(n)+"'", false, true));
						numofExistRecord = total + numofExistRecord;
					}
				}
				Assert.assertEquals("Expected total number of transactions in the DB is incorrect!", expectedNumOfTxnInDB, numofExistRecord);
				closeDBConnection(stmt);
			}
	
	/**
	 * Compare number of account attributes in the db
	 * @param conduitFile
	 * @param expectedNumOfAcctAttrInDB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkNumOfAcctAttr(
			String conduitFile, 
			int expectedNumOfAcctAttrInDB) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				List<Object> stmt = connectToDB();
				List<String> acctAttr = getElementValueInConduitXMLFile(conduitFile, "acctlst", "alst", "a", "", "n");
				List<String> acctUID = getElementValueInConduitXMLFile(conduitFile, "acct", "", "uid");
				List<String> acctCUID = getElementValueInConduitXMLFile(conduitFile, "acct", "", "cuid");
				int numofExistRecord = 0;
				for (int a=0; a<acctUID.size(); a++)
				{
					for (int n=0; n<acctAttr.size(); n++)
					{
							int total = Integer.parseInt(runQuery(stmt,"select count(*) from account_attribute as t1, d3_account as t2, company as t3\r\n" + 
									"where t1.account_id=t2.id and t2.company_id=t3.id and t2.source_account_id='"+acctUID.get(a)+"'\r\n" + 
									"and t3.source_company_id='"+acctCUID.get(a)+"' and t1.name = '"+acctAttr.get(n)+"'", false, true));
							numofExistRecord = total + numofExistRecord;
							total = 0;
					}
				}
				
				Assert.assertEquals("Expected total number of account attributes in the DB is incorrect!", expectedNumOfAcctAttrInDB, numofExistRecord);
				closeDBConnection(stmt);
			}
	
	/**
	 * Validate account list elements by comparing xml data with what is the db
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateAccountListElement (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck, valueInXML;	
		List<String> valuesInXML = new ArrayList<String>();
		List<String> acctUIDs = getElementValueInConduitXMLFile(conduitFile, "acct", "", "uid");
		List<String> txnUIDs = getElementValueInConduitXMLFile(conduitFile, "txn", "", "uid");
		List<String> acctAttrs = getElementValueInConduitXMLFile(conduitFile, "acct", "alst", "a", "", "n");
		for (int u = 0; u <acctUIDs.size(); u++)
		{
			//validate uid
			elementToCheck = "Account UID";
			query = "select source_account_id from d3_account where source_account_id = '"+acctUIDs.get(u)+"'";
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  acctUIDs.get(u));
			//validate cuid
			elementToCheck = "Account CUID";
			query = "select t2.source_company_id from d3_account as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "", "cuid");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate produid
			elementToCheck = "Account ProdUID";
			query = "select t2.source_product_id from d3_account as t1, account_product as t2 \r\n" + 
					"where t1.account_product_id=t2.id and source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "produid", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate nm
			elementToCheck = "Account Name";
			query = "select account_name from d3_account where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "nm", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate rttn
			elementToCheck = "Account Routing Transit Num";
			query = "select routing_transit_number from d3_account where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "rttn", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate nbr
//			elementToCheck = "Account Num";
//			query = "select account_number from d3_account where source_account_id = '"+acctUIDs.get(u)+"'";
//			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "nbr", "");
//			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate bal
			elementToCheck = "Account Balance";
			query = "select balance from d3_account where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "bal", "");
			valueInXML = valuesInXML.get(u);
			while (valueInXML.split("\\.")[1].length()<6)
			{
				valueInXML = valueInXML + "0";
			}
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
			//validate sts
			elementToCheck = "Account Status";
			query = "select [account_status] from d3_account where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "sts", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate cc
			elementToCheck = "Account Currency Code";
			query = "select [currency_code] from d3_account where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "cc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate avbal
			elementToCheck = "Account Avail Balance";
			query = "select [available_balance] from [d3_account] where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "avbal", "");
			valueInXML = valuesInXML.get(u);
			while (valueInXML.split("\\.")[1].length()<6)
			{
				valueInXML = valueInXML + "0";
			}
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
			//validate rstr
			elementToCheck = "Account Restricted";
			query = "select [restricted] from [d3_account] where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "rstr", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate estmt
			elementToCheck = "Account EStatement";
			query = "select statement_preference_type from [d3_account] where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "estmt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate opndt
			elementToCheck = "Account Open Date";
			query = "select [account_open_date] from [d3_account] where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "opndt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate clsdt
			elementToCheck = "Account Close Date";
			query = "select [account_closed_date] from [d3_account] where source_account_id = '"+acctUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "clsdt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));

			for (int a = 0; a <acctAttrs.size(); a++)
			{
				//validate name
				elementToCheck = "Account Attribute:Name";
				query = "select t2.name from [d3_account] as t1, [account_attribute] as t2\r\n" + 
						"  where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.name='"+acctAttrs.get(a)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  acctAttrs.get(a));
				//validate type
				elementToCheck = "Account Attribute:Type";
				query = "select t2.type from [d3_account] as t1, [account_attribute] as t2\r\n" + 
						"  where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.name='"+acctAttrs.get(a)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "alst", "a", "", "t");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
				//validate value
				elementToCheck = "Account Attribute:Value";
				query = "select t2.value from [d3_account] as t1, [account_attribute] as t2\r\n" + 
						"  where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.name='"+acctAttrs.get(a)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acctlst", "acct", "alst", "a", "");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
			}
			for (int t = 0; t <txnUIDs.size(); t++)
			{
				//validate uid
				elementToCheck = "Transaction UID";
				query = "select t2.source_tx_id from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  txnUIDs.get(u));
				//validate tp
				elementToCheck = "Transaction Type";
				query = "select t2.type from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "tp");
				valueInXML = valuesInXML.get(t);
				if (valueInXML.toLowerCase().equals("d"))
				{
					valueInXML = "1";
				}else if (valueInXML.toLowerCase().equals("c"))
				{
					valueInXML = "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate am
				elementToCheck = "Transaction Amount";
				query = "select t2.amount from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "am");
				valueInXML = valuesInXML.get(t);
				while (valueInXML.split("\\.")[1].length()<6)
				{
					valueInXML = valueInXML + "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate pa
				elementToCheck = "Transaction Principal Amount";
				query = "select t2.principal_amount from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "pa");
				valueInXML = valuesInXML.get(t);
				while (valueInXML.split("\\.")[1].length()<6)
				{
					valueInXML = valueInXML + "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate ia
				elementToCheck = "Transaction Interest Amount";
				query = "select t2.interest_amount from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "ia");
				valueInXML = valuesInXML.get(t);
				while (valueInXML.split("\\.")[1].length()<6)
				{
					valueInXML = valueInXML + "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate oa
				elementToCheck = "Transaction Other Amount";
				query = "select t2.other_amount from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "oa");
				valueInXML = valuesInXML.get(t);
				while (valueInXML.split("\\.")[1].length()<6)
				{
					valueInXML = valueInXML + "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate pd
				elementToCheck = "Transaction Post Date";
				query = "select t2.post_date from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "pd");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate pn
				elementToCheck = "Transaction Pending";
				query = "select t2.pending from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "pn");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate mc
				elementToCheck = "Transaction MCC";
				query = "select t2.merchant_category_code from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "mc");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate rb
				elementToCheck = "Transaction Running Balance";
				query = "select t2.running_balance from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "rb");
				valueInXML = valuesInXML.get(t);
				while (valueInXML.split("\\.")[1].length()<6)
				{
					valueInXML = valueInXML + "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate od
				elementToCheck = "Transaction Origination Date";
				query = "select t2.origination_date from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "od");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate tc
				elementToCheck = "Transaction Code";
				query = "select t2.tran_code from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "tc");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate cn
				elementToCheck = "Transaction CheckNum";
				query = "select t2.checknum from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "cn");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate dn
				elementToCheck = "Transaction DepositNum";
				query = "select t2.deposit_num from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "dn");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate im
				elementToCheck = "Transaction Image";
				query = "select t2.image_id from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "im");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate ps
				elementToCheck = "Transaction PostingSeq";
				query = "select t2.posting_seq from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "ps");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate nm
				elementToCheck = "Transaction Name";
				query = "select t2.name from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "nm", "");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate mm
				elementToCheck = "Transaction Memo";
				query = "select t2.memo from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "mm", "");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
				//validate fid
				elementToCheck = "Transaction fid";
				query = "select t2.ofx_id from [d3_account] as t1, [d3_transaction] as t2\r\n" + 
						"where t1.id=t2.account_id and t1.source_account_id='"+acctUIDs.get(u)+"' and t2.source_tx_id='"+txnUIDs.get(t)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "txn", "", "fid");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(t));
			}
		}

		closeDBConnection(_stmt);
	}
	
	/**
	 * Compare number of users in the DB
	 * @param conduitFile
	 * @param expectedNumOfUsersInDB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkNumOfUsers(
			String conduitFile, 
			int expectedNumOfUsersInDB) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{				
				List<Object> stmt = connectToDB();
				List<String> userUIDs = getElementValueInConduitXMLFile(conduitFile, "usr", "", "uid");
				int total = 0;
				for (int u = 0; u <userUIDs.size(); u++)
				{
					total = total +  Integer.parseInt(runQuery(stmt,"select count(*) from d3_user where host_id = '"+userUIDs.get(u)+"'", false, true));
				}
				Assert.assertEquals("Expected total number of user in the DB is incorrect!", expectedNumOfUsersInDB, total);
				closeDBConnection(stmt);
			}
	
	/**
	 * Compare number of user_acounts in the DB
	 * @param conduitFile
	 * @param expectedNumOfUserAcctInDB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkNumOfUserAcct(
			String conduitFile, 
			int expectedNumOfUserAcctInDB) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				List<Object> stmt = connectToDB();
				List<String> login = getElementValueInConduitXMLFile(conduitFile, "usr", "login", "");
				int total = 0;
				for (int l = 0; l <login.size(); l++)
				{
					total = total +  Integer.parseInt(runQuery(stmt,"select count(*) from d3_account as t1, user_account as t2, d3_user as t3\r\n" + 
							"where t1.id=t2.account_id and t3.id=t2.user_id and t3.login_id = '"+login.get(l)+"'", false, true));
				}
				Assert.assertEquals("Expected total number of user_account records in the DB is incorrect!", expectedNumOfUserAcctInDB, total);
				closeDBConnection(stmt);
			}
	
	/**
	 * Compare number of persons in the DB
	 * @param conduitFile
	 * @param expectedNumOfPersonsInDB
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkNumOfPersons(
			String conduitFile, 
			int expectedNumOfPersonsInDB) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				List<Object> stmt = connectToDB();
				List<String> userUIDs = getElementValueInConduitXMLFile(conduitFile, "usr", "", "uid");
				int total = 0;
				for (int u = 0; u <userUIDs.size(); u++)
				{
					total = total +  Integer.parseInt(runQuery(stmt,"select count(*) from person as t1, d3_user as t2 where t1.id=t2.person_id and t2.host_id = '"+userUIDs.get(u)+"'", false, true));
				}
				Assert.assertEquals("Expected total number of person in the DB is incorrect!", expectedNumOfPersonsInDB, total);
				closeDBConnection(stmt);
			}
			
	/**
	 * Validate userList element values by comparing with what is in the db
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateUserListElement (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException{
		
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck, valueInXML;	
		List<String> valuesInXML = new ArrayList<String>();
		List<String> userAcctUIDs = getElementValueInConduitXMLFile(conduitFile, "usracct", "", "auid");
		List<String> userUIDs = getElementValueInConduitXMLFile(conduitFile, "usr", "", "uid");
		for (int u = 0; u <userUIDs.size(); u++)
		{
			//validate uid
			elementToCheck = "USER UID";
			query = "select host_id from d3_user where host_id = '"+userUIDs.get(u)+"'";
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  userUIDs.get(u));
			//validate cuid
			elementToCheck = "USER CUID";
			query = "select t2.source_company_id from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "", "cuid");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate login
			elementToCheck = "USER Login";
			query = "select t1.login_id from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "login", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate enroll
			elementToCheck = "USER enrolled";
			query = "select t1.enrolled from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "enrl", "");
			if (valuesInXML.get(u).equals("true"))
			{
				valuesInXML.set(u, "1");
			} else if (valuesInXML.get(u).equals("true"))
			{
				valuesInXML.set(u, "0");
			}
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate txid
//			elementToCheck = "USER TaxID";
//			query = "select t1.tax_id from d3_user as t1, company as t2 \r\n" + 
//					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
//			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "txid", "");
//			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate txtp
			elementToCheck = "USER TaxIdType";
			query = "select t1.tax_id_type from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "txtp", "");
			if(valuesInXML.get(u).equals("l"))
			{
				valueInXML = "interntional";
			}else if(valuesInXML.get(u).equals("s"))
			{
				valueInXML = "ssn";
			}else if(valuesInXML.get(u).equals("t"))
			{
				valueInXML = "tin";
			}else{
				valueInXML = valuesInXML.get(u);
			}
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
			//validate lock
			elementToCheck = "USER Lock";
			query = "select t1.locked from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "lock", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate enbl
			elementToCheck = "USER Enabled";
			query = "select t1.user_enabled from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "enbl", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate emlopt
			elementToCheck = "USER EmailOptOut";
			query = "select t1.email_opt_out from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "emlopt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate dob
			elementToCheck = "USER DateOfBirth";
			query = "select t1.date_of_birth from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "dob", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate g
			elementToCheck = "USER Gender";
			query = "select t1.gender from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "g", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate bpsts
			elementToCheck = "USER BillpayStatus";
			query = "select t1.billpay_status from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "bpsts", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate bpid
			elementToCheck = "USER BillpaySubsId";
			query = "select t1.billpay_subscriber_id from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "bpid", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate m
			elementToCheck = "USER Mobile";
			query = "select t1.mobile from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "m", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate crsc
			elementToCheck = "USER CreditScore";
			query = "select t1.credit_score from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "crsc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate crdt
			elementToCheck = "USER CreditScoreDate";
			query = "select t1.credit_score_date from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "crdt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate cls
			elementToCheck = "USER Class";
			query = "select t1.user_class from d3_user as t1, company as t2 \r\n" + 
					"where t1.company_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "cls", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/fn
			elementToCheck = "USER/Person FirstName";
			query = "select t2.first_name from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "fn", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/mn
			elementToCheck = "USER/Person MiddleName";
			query = "select t2.middle_name from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "mn", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/ln
			elementToCheck = "USER/Person LastName";
			query = "select t2.last_name from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "ln", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/pem
			elementToCheck = "USER/Person PrimaryEmail";
			query = "select t2.primary_email from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "pem", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/aem
			elementToCheck = "USER/Person AlternateEmail";
			query = "select t2.alternate_email from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "aem", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/hph
			elementToCheck = "USER/Person HomePhone";
			query = "select t2.home_phone from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "hph", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u).replace(" ", "").replace("-", "").trim());
			//validate psn/wph
			elementToCheck = "USER/Person WorkPhone";
			query = "select t2.work_phone from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "wph", "");
			valueInXML = (valuesInXML.get(u).replace(" ", "").replace("-", "").trim());
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML.substring(0, (valueInXML.length()-7)) 
					+ " " + valueInXML.substring((valueInXML.length()-7), (valueInXML.length()-4)) 
					+ " " + valueInXML.substring((valueInXML.length()-4), valueInXML.length()));
			//validate psn/mph
			elementToCheck = "USER/Person MobilePhone";
			query = "select t2.mobile_phone from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "mph", "");
			valueInXML = (valuesInXML.get(u).replace(" ", "").replace("-", "").trim());
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML.substring(0, (valueInXML.length()-7)) 
					+ " " + valueInXML.substring((valueInXML.length()-7), (valueInXML.length()-4)) 
					+ " " + valueInXML.substring((valueInXML.length()-4), valueInXML.length()));
			//validate psn/emp
			elementToCheck = "USER/Person Employee";
			query = "select t2.employee from d3_user as t1, person as t2\r\n" + 
					"where t1.person_id=t2.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "psn", "emp", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/address1
			elementToCheck = "USER/Phys/Address1";
			query = "select t3.address1 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a1", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/address2
			elementToCheck = "USER/Phys/Address2";
			query = "select t3.address2 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a2", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/address3
			elementToCheck = "USER/Phys/Address3";
			query = "select t3.address3 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a3", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/address4
			elementToCheck = "USER/Phys/Address4";
			query = "select t3.address4 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "a4", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/city
			elementToCheck = "USER/Phys/City";
			query = "select t3.city from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "ct", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/state
			elementToCheck = "USER/Phys/State";
			query = "select t3.state from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "st", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/countryCode
			elementToCheck = "USER/Phys/coutryCode";
			query = "select t3.country_code from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "cc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/PostalCode
			elementToCheck = "USER/Phys/PostalCode";
			query = "select t3.postal_code from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "pc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/ls
			elementToCheck = "USER/Phys/Latitude";
			query = "select t3.latitude from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "lt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/paddr/ln
			elementToCheck = "USER/Phys/Longitude";
			query = "select t3.longitude from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.physical_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "paddr", "ln", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr
			elementToCheck = "USER/Mailing/Address1";
			query = "select t3.address1 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a1", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/address2
			elementToCheck = "USER/Mailing/Address2";
			query = "select t3.address2 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a2", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/address3
			elementToCheck = "USER/Mailing/Address3";
			query = "select t3.address3 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a3", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/address4
			elementToCheck = "USER/Mailing/Address4";
			query = "select t3.address4 from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "a4", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/city
			elementToCheck = "USER/Mailing/City";
			query = "select t3.city from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "ct", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/state
			elementToCheck = "USER/Mailing/State";
			query = "select t3.state from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "st", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/countryCode
			elementToCheck = "USER/Mailing/coutryCode";
			query = "select t3.country_code from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "cc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/PostalCode
			elementToCheck = "USER/Mailing/PostalCode";
			query = "select t3.postal_code from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "pc", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/ls
			elementToCheck = "USER/Mailing/Latitude";
			query = "select t3.latitude from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "lt", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));
			//validate psn/maddr/ln
			elementToCheck = "USER/Mailing/Longitude";
			query = "select t3.longitude from d3_user as t1, person as t2, [address] as t3\r\n" + 
					"where t1.person_id=t2.id and t2.mailing_address_id=t3.id and host_id = '"+userUIDs.get(u)+"'";
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "maddr", "ln", "");
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(u));

			for (int a = 0; a <userAcctUIDs.size(); a++)
			{
				//validate auid
				elementToCheck = "USER AccountUID";
				query = "select t3.source_account_id from d3_user as t1, [user_account] as t2, [d3_account] as t3\r\n" + 
						"where t1.id=t2.user_id and t2.account_id=t3.id and t3.source_account_id = '"+userAcctUIDs.get(u)+"'\r\n" + 
						"and host_id = '"+userUIDs.get(u)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  userAcctUIDs.get(a));
				//validate association
				elementToCheck = "USER Association";
				query = "select t2.associated from d3_user as t1, [user_account] as t2, [d3_account] as t3\r\n" + 
						"where t1.id=t2.user_id and t2.account_id=t3.id and t3.source_account_id = '"+userAcctUIDs.get(u)+"'\r\n" + 
						"and host_id = '"+userUIDs.get(u)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usracct", "", "a");
				valueInXML = valuesInXML.get(a);
				if (valueInXML.trim().toLowerCase().equals("a"))
				{
					valueInXML = "1";
				}else
				{
					valueInXML = "0";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML); 
			}
			List<String> acctAttrs = getElementValueInConduitXMLFile(conduitFile, "usr", "alst", "a", "", "n");
			for (int a = 0; a < acctAttrs.size(); a++)
			{
				if(acctAttrs.get(a).equals("elementDoesNotExist"))
				{
					break;
				}
				//validate name
				elementToCheck = "User Attribute:Name";
				query = "select count(*) from user_attribute as t1, d3_user as t2 \r\n" + 
						" where t1.user_id=t2.id and t2.host_id = '"+userUIDs.get(u)+"' and t1.name = '"+acctAttrs.get(a)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  "1");
				//validate type
				elementToCheck = "User Attribute:Type";
				query = "select t1.type from user_attribute as t1, d3_user as t2 \r\n" + 
						" where t1.user_id=t2.id and t2.host_id = '"+userUIDs.get(u)+"' and t1.name = '"+acctAttrs.get(a)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "alst", "a", "", "t");
				if(valuesInXML.get(a).equals("b"))
				{
					valueInXML = "BOOLEAN";
				} else if(valuesInXML.get(a).equals("d"))
				{
					valueInXML = "DATE";
				}
				else if(valuesInXML.get(a).equals("t"))
				{
					valueInXML = "DATE_TIME";
				}
				else if(valuesInXML.get(a).equals("c"))
				{
					valueInXML = "DECIMAL";
				}
				else if(valuesInXML.get(a).equals("i"))
				{
					valueInXML = "INTEGER";
				}
				else if(valuesInXML.get(a).equals("m"))
				{
					valueInXML = "MONEY";
				}
				else if(valuesInXML.get(a).equals("p"))
				{
					valueInXML = "PERCENT";
				}
				else if(valuesInXML.get(a).equals("s"))
				{
					valueInXML = "STRING";
				}else{
					valueInXML = valuesInXML.get(a);
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate visible
				elementToCheck = "User Attribute:Visible";
				query = "select t1.is_displayable from user_attribute as t1, d3_user as t2 \r\n" + 
						" where t1.user_id=t2.id and t2.host_id = '"+userUIDs.get(u)+"' and t1.name = '"+acctAttrs.get(a)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "alst", "a", "", "v");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
				//validate displayOrder
				elementToCheck = "User Attribute:DisplayOrder";
				query = "select t1.display_order from user_attribute as t1, d3_user as t2 \r\n" + 
						" where t1.user_id=t2.id and t2.host_id = '"+userUIDs.get(u)+"' and t1.name = '"+acctAttrs.get(a)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usr", "alst", "a", "", "d");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
				//validate value
				elementToCheck = "User Attribute:Value";
				query = "select t1.value from user_attribute as t1, d3_user as t2 \r\n" + 
						" where t1.user_id=t2.id and t2.host_id = '"+userUIDs.get(u)+"' and t1.name = '"+acctAttrs.get(a)+"'";
				valuesInXML = getElementValueInConduitXMLFile(conduitFile, "usrlst", "usr", "alst", "a", "");
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valuesInXML.get(a));
			}
		}

		closeDBConnection(_stmt);
	}
	
	/**
	 * Validate user account association
	 * @param conduitFile
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void checkAccUserAssocation(String conduitFile) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		List<String> userAssoc = getElementValueInConduitXMLFile(conduitFile, "usracct", "", "a");
		List<String> userAUID = getElementValueInConduitXMLFile(conduitFile, "usracct", "", "auid");
		for (int a=0; a<userAUID.size(); a++)
		{
			String associated = runQuery(stmt,"select t1.associated from user_account as t1, d3_account as t2\r\n" + 
					"where t1.account_id=t2.id and t2.source_account_id='"+userAUID.get(a)+"'", false, true);
			if (userAssoc.get(a).toLowerCase().trim().equals("d"))
			{		
				Assert.assertEquals("Account("+userAUID.get(a)+") is not disassociated with the user!", "0", associated);
			}else{
				Assert.assertEquals("Account("+userAUID.get(a)+") is not associated with the user!", "1", associated);
			}
		}
		
		closeDBConnection(stmt);
	}
	
	/**
	 * Delete system events
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void deleteSystemEvents() throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
	{	
		runQuery(connectToDB(),"delete FROM system_event where description LIKE '%Unexpected Exception: null'", true, false);
	}
	
	/**
	 * Validate alert destination values in the DB
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateAlertDest (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
		
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck, valueInXML = null;	
		List<String> usrmigUIDs = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		List<String> alertdestUIDs = getElementValueInConduitXMLFile(conduitFile, "alertdest", "", "uid");
		for (int u = 0; u <usrmigUIDs.size(); u++)
		{
			for (int a = 0; a <alertdestUIDs.size(); a++)
			{
				//validate nm and tp
				elementToCheck = "AlertDest Name";
				List<String> alertDestName = getElementValueInConduitXMLFile(conduitFile, "alertdest", "nm", "");
				List<String> alertDestType = getElementValueInConduitXMLFile(conduitFile, "alertdest", "tp", "");
				query = "  select t1.name from alert_user_dest as t1, d3_user as t2, alert_dest_type as t3\r\n" + 
						"  where t1.user_id=t2.id and t1.dest_type_id=t3.id and t1.name = '"+alertDestName.get(a)+"' and t3.dest_type = '"+alertDestType.get(a).toUpperCase()+"'\r\n" + 
						"  and t2.host_id = '"+usrmigUIDs.get(u)+"' ";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  alertDestName.get(a));
				//dest
				elementToCheck = "AlertDest Dest";
				List<String> alertDestDest = getElementValueInConduitXMLFile(conduitFile, "alertdest", "dest", "");
				if (!alertDestDest.get(a).equals("elementDoesNotExist"))
				{
					query = "  select t1.dest_address from alert_user_dest as t1, d3_user as t2, alert_dest_type as t3\r\n" + 
							"  where t1.user_id=t2.id and t1.dest_type_id=t3.id and t1.name = '"+alertDestName.get(a)+"' and t3.dest_type = '"+alertDestType.get(a).toUpperCase()+"'\r\n" + 
							"  and t2.host_id = '"+usrmigUIDs.get(u)+"' ";
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  alertDestDest.get(a));
				}
				//prDest
				elementToCheck = "AlertDest PrimaryDest";
				List<String> alertDestPrimDest = getElementValueInConduitXMLFile(conduitFile, "alertdest", "pridest", "");
				if (!alertDestPrimDest.get(a).equals("elementDoesNotExist"))
				{
					query = "  select t1.prime_dest from alert_user_dest as t1, d3_user as t2, alert_dest_type as t3\r\n" + 
							"  where t1.user_id=t2.id and t1.dest_type_id=t3.id and t1.name = '"+alertDestName.get(a)+"' and t3.dest_type = '"+alertDestType.get(a).toUpperCase()+"'\r\n" + 
							"  and t2.host_id = '"+usrmigUIDs.get(u)+"' ";
					switch(alertDestPrimDest.get(a))
					{
						case "true": valueInXML = "1";
								 break;
						case "false": valueInXML = "0";
						 		 break;
						default: valueInXML = alertDestPrimDest.get(a);
								 break;
					}
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				}
				//altDest
				elementToCheck = "AlertDest AltDest";
				List<String> alertDestAltDest = getElementValueInConduitXMLFile(conduitFile, "alertdest", "altdest", "");
				if (!alertDestAltDest.get(a).equals("elementDoesNotExist"))
				{
					query = "  select t1.alt_dest from alert_user_dest as t1, d3_user as t2, alert_dest_type as t3\r\n" + 
							"  where t1.user_id=t2.id and t1.dest_type_id=t3.id and t1.name = '"+alertDestName.get(a)+"' and t3.dest_type = '"+alertDestType.get(a).toUpperCase()+"'\r\n" + 
							"  and t2.host_id = '"+usrmigUIDs.get(u)+"' ";
					switch(alertDestAltDest.get(a))
					{
						case "true": valueInXML = "1";
								 break;
						case "false": valueInXML = "0";
						 		 break;
						default: valueInXML = alertDestAltDest.get(a);
								 break;
					}
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				}
			}
		}

		closeDBConnection(_stmt);
	}
	
	/**
	 * Compare alerts with what is in the XML versus in the DB
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateAlert (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
		
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck, valueInXML = null;	
		List<String> usrmigUIDs = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		List<String> alertUIDs = getElementValueInConduitXMLFile(conduitFile, "alert", "", "uid");
		for (int u = 0; u <usrmigUIDs.size(); u++)
		{
			for (int a = 0; a <alertUIDs.size(); a++)
			{
				//validate alert tp
				elementToCheck = "AlertType";
				List<String> alertType = getElementValueInConduitXMLFile(conduitFile, "alert", "tp", "");
				String alertTp = "";
				switch (alertType.get(a))
				{ 
					case "transfer.success": alertTp = "";
							 break;
					case "transfer.failure": alertTp = "";
					 		 break;
					case "credit.deposit": alertTp = "ACCOUNT_CREDIT";
							 break;
					case "credit.threshold": alertTp = "";
			 		 		 break;
					case "debit.threshold": alertTp = "";
							 break;
					case "periodic.balance": alertTp = "";
							 break;
					case "check.number.cleared": alertTp = "";
			 		 		 break;
					case "balance.threshold": alertTp = "BALANCE_THRESHOLD";
							 break;
					case "budget.total.threshold": alertTp = "BUDGET_TOTAL_THRESHOLD";
							 break;
					case "budget.category.threshold": alertTp = "BUDGET_CATEGORY_THRESHOLD";
							 break;
					case "account.reminder": alertTp = "REMINDER";
					 		 break;
					case "transaction.merchant": alertTp = "TRANSACTION_MERCHANT";
					 		 break;
					case "transaction.amount": alertTp = "TRANSACTION_AMOUNT";
						 	 break;
				}
				query = "  select t2.alert_type from alert_user_alert as t1, [alert_definition] as t2, d3_user as t3\r\n" + 
						"  where t1.alert_id=t2.id and t2.alert_type='"+alertTp+"' and t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  alertTp);
				//validate alert desc
				elementToCheck = "AlertDescription";
				List<String> alertDesc = getElementValueInConduitXMLFile(conduitFile, "alert", "desc", "");
				if (!alertDesc.get(a).equals("elementDoesNotExist"))
				{
					query = "  select t1.description from alert_user_alert as t1, [alert_definition] as t2, d3_user as t3\r\n" + 
							"  where t1.alert_id=t2.id and t2.alert_type='"+alertTp+"' and t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  alertDesc.get(a));
					//validate alert enbl
					elementToCheck = "AlertEnabled";
					List<String> alertEnabled = getElementValueInConduitXMLFile(conduitFile, "alert", "enbl", "");
					switch(alertEnabled.get(a))
					{
						case "true": valueInXML = "1";
								 break;
						case "false": valueInXML = "0";
						 		 break;
						default: valueInXML = alertEnabled.get(a);
								 break;
					}
					query = "  select t1.alert_enabled from alert_user_alert as t1, [alert_definition] as t2, d3_user as t3\r\n" + 
							"  where t1.alert_id=t2.id and t2.alert_type='"+alertTp+"' and t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				}
				//validate alert freq
				elementToCheck = "AlertFrequency";
				List<String> alertFrequency = getElementValueInConduitXMLFile(conduitFile, "alert", "freq", "");
				if (!alertFrequency.get(a).equals("elementDoesNotExist"))
				{
					switch(alertFrequency.get(a))
					{
						case "daily": valueInXML = "DAYS";
								 break;
						case "always": valueInXML = "ALWAYS";
						 		 break;
						case "hourly": valueInXML = alertFrequency.get(a).toUpperCase();
				 		 		 break;
						case "onetime": valueInXML = alertFrequency.get(a).toUpperCase();
				 		 		 break;
						case "semimonthly": valueInXML = alertFrequency.get(a).toUpperCase();
								 break;
						case "semiweekly": valueInXML = alertFrequency.get(a).toUpperCase();
								 break;
						case "weekly": valueInXML = alertFrequency.get(a).toUpperCase();
				 		 		 break;
						case "monthly": valueInXML = "MONTHLY";
				 		 		 break;
						case "quarterly": valueInXML = "QUARTERLY";
								 break;
						case "semiannually": valueInXML = alertFrequency.get(a).toUpperCase();
				 		 		 break;
						case "annually": valueInXML = "ANNUALLY";
				 		 		 break;
					}
					query = "  select t1.delivery_frequency from alert_user_alert as t1, [alert_definition] as t2, d3_user as t3\r\n" + 
							"  where t1.alert_id=t2.id and t2.alert_type='"+alertTp+"' and t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				}
				
				//validate alert dnd
				elementToCheck = "AlertDoNotDisturb";
				List<String> alertDnd = getElementValueInConduitXMLFile(conduitFile, "alert", "dnd", "");
				if (!alertDnd.get(a).equals("elementDoesNotExist"))
				{
					query = "  select t1.dnd_action from alert_user_alert as t1, [alert_definition] as t2, d3_user as t3\r\n" + 
							"  where t1.alert_id=t2.id and t2.alert_type='"+alertTp+"' and t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  alertDnd.get(a).toUpperCase());
				}
				
				//validate alert alst
				List<String> alertAttrName = getElementValueInConduitXMLFile(conduitFile, "alert", "alst", "a","", "n");
				if (!alertAttrName.get(a).equals("elementDoesNotExist"))
				{
					for (int d=0; d<alertAttrName.size(); d++)
					{
						elementToCheck = "AlertAttributeValue";
						List<String> alertAttrValue = getElementValueInConduitXMLFile(conduitFile, "alertlst", "alert", "alst","a", "");
						query = "select t4.value from alert_user_alert as t1, [alert_definition] as t2, d3_user as t3, alert_user_alert_prop as t4\r\n" + 
								"  where t1.alert_id=t2.id and t2.alert_type='"+alertTp+"' and t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'\r\n" + 
								"  and t1.id=t4.user_alert_id and t4.name = '"+alertAttrName.get(d)+"'";
						compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  alertAttrValue.get(d));
					}
				}
				
				//validate alert destList
				elementToCheck = "AlertDestName linked with Alert";
				List<String> alertDest = getElementValueInConduitXMLFile(conduitFile, "alertdest", "", "uid");
				List<String> alertDestName = getElementValueInConduitXMLFile(conduitFile, "alertdest", "nm", "");
				List<String> destAlertLinked = getElementValueInConduitXMLFile(conduitFile, "alert", "destlst","dest", "", "uid");
				valueInXML = "not found";
				for (int d=0; d<alertDest.size(); d++)
				{
					for (int dd=0; dd<destAlertLinked.size(); dd++)
					{
						if(alertDest.get(d).equals(destAlertLinked.get(dd)))
						{
							valueInXML = alertDestName.get(dd);
							break;
						}
					}
					if (!valueInXML.equals("not found"))
					{
						break;
					}
				}
				query = "select t5.name from alert_user_alert as t1, [alert_definition] as t2, d3_user as t3, alert_user_dest_join as t4, alert_user_dest as t5\r\n" + 
						"  where t1.alert_id=t2.id and t2.alert_type='"+alertTp+"' and t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"' \r\n" + 
						"  and t4.user_alert_id=t1.id and t4.user_dest_id=t5.id";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
			}
		}

		closeDBConnection(_stmt);
	}
	
	/**
	 * Compare the message loaded with conduit in the DB
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateMessage (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
		
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck;	
		List<String> usrmigUIDs = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		List<String> msgUIDs = getElementValueInConduitXMLFile(conduitFile, "msg", "", "uid");
		for (int u = 0; u <usrmigUIDs.size(); u++)
		{
			for (int a = 0; a <msgUIDs.size(); a++)
			{
				//validate message
				elementToCheck = "MessageSubject";
				List<String> msgType = getElementValueInConduitXMLFile(conduitFile, "msg", "tp", "");
				List<String> msgSubject = getElementValueInConduitXMLFile(conduitFile, "msg", "sb", "");
				List<String> msgBody = getElementValueInConduitXMLFile(conduitFile, "msg", "bd", "");
				List<String> msgStatus = getElementValueInConduitXMLFile(conduitFile, "msg", "sts", "");
				if (msgBody.get(a).equals("elementDoesNotExist"))
				{
					query = "select t1.subject from user_message as t1, d3_user as t2\r\n" + 
							"where t1.user_id=t2.id and t2.host_id = '"+usrmigUIDs.get(u)+"' and \r\n" + 
							"t1.source='"+msgType.get(a)+ 
							"' and t1.status = '"+msgStatus.get(a)+"'";
					if (msgStatus.get(a).equals("elementDoesNotExist"))
					{
						query = "select t1.subject from user_message as t1, d3_user as t2\r\n" + 
								"where t1.user_id=t2.id and t2.host_id = '"+usrmigUIDs.get(u)+"' and \r\n" + 
								"t1.source='"+msgType.get(a)+ 
								"' and t1.status = 'NEW'";
					}
				}else{
					query = "select t1.subject from user_message as t1, d3_user as t2\r\n" + 
							"where t1.user_id=t2.id and t2.host_id = '"+usrmigUIDs.get(u)+"' and \r\n" + 
							"t1.source='"+msgType.get(a)+"' and t1.body = '"+msgBody.get(a)+"'\r\n" + 
							" and t1.status = '"+msgStatus.get(a)+"'";
				}
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  msgSubject.get(a));
			}
		}
		closeDBConnection(_stmt);
	}
	
	/**
	 * Validate category loaded with conduit in the DB
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateCategory (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
		
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck;	
		List<String> usrmigUIDs = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		List<String> catUIDs = getElementValueInConduitXMLFile(conduitFile, "usrmig", "catlst", "cat", "", "uid");
		for (int u = 0; u <usrmigUIDs.size(); u++)
		{
			for (int a = 0; a <catUIDs.size(); a++)
			{
				//validate category
				elementToCheck = "CategoryGroup";
				List<String> catType = getElementValueInConduitXMLFile(conduitFile, "cat", "tp", "");
				List<String> catLevel = getElementValueInConduitXMLFile(conduitFile, "cat", "lvl", "");
				List<String> catGroup = getElementValueInConduitXMLFile(conduitFile, "cat", "grp", "");
				String categoryType = "";
				switch(catType.get(a))
				{
					case "expense": categoryType = "EXPENSE_DISCRETIONARY";
							 break;
					case "income": categoryType = "INCOME";
				 			 break;
				}
				query = "select t1.category_group from category as t1, d3_user as t2\r\n" + 
						"where t1.user_id=t2.id and t2.host_id = '"+usrmigUIDs.get(u)+"' and\r\n" + 
						"t1.category_type = '"+categoryType+"' and t1.category_owner = '"+catLevel.get(a)+"' and\r\n" + 
						"t1.category_group = '"+catGroup.get(a)+"' and t1.category_name = '"+catGroup.get(a)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  catGroup.get(a));
				//validate categoryName
				elementToCheck = "CategoryName";
				List<String> catName = getElementValueInConduitXMLFile(conduitFile, "cat", "nm", "");
				if (!catName.get(a).equals("elementDoesNotExist"))
				{
					query = "select t1.category_name from category as t1, d3_user as t2\r\n" + 
							"where t1.user_id=t2.id and t2.host_id = '"+usrmigUIDs.get(u)+"' and\r\n" + 
							"t1.category_type = '"+categoryType+"' and t1.category_owner = '"+catLevel.get(a)+"' and\r\n" + 
							"t1.category_group = '"+catGroup.get(a)+"' and t1.category_name = '"+catName.get(a)+"'";
					compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  catName.get(a));
				}
			}
		}
		closeDBConnection(_stmt);
	}

	/**
	 * Validate number of userMigrationList data in the DB
	 * @param conduitFile
	 * @param expectedNumOfAlertDests
	 * @param expectedNumofAlerts
	 * @param expectedNumOfMsgs
	 * @param expectedNumOfCats
	 * @param expecntedNumOfTnxSplits
	 * @param expecntedNumAlertDestJoin
	 * @param expecntedNumOfAlertProp
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void checkNumOfUserMigrationList(
			String conduitFile, 
			int expectedNumOfAlertDests,
			int expectedNumofAlerts,
			int expectedNumOfMsgs,
			int expectedNumOfCats,
			int expectedNumOfTnxSplits,
			int expectedNumAlertDestJoin,
			int expectedNumOfAlertProp) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException   
			{
				List<Object> stmt = connectToDB();
				//List<String> catGroup = getElementValueInConduitXMLFile(conduitFile, "cat", "grp", "");
				List<String> usrUid = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
				
			
				int total = 0;
				total = Integer.parseInt(runQuery(stmt,"select count(*) from alert_user_dest as t1, d3_user as t2\r\n" + 
						                               "where t1.user_id = t2.id and t2.host_id='"+usrUid.get(0)+"'", false, true));
				Assert.assertEquals("Expected total number of alertDestinations in the DB is incorrect!", expectedNumOfAlertDests, total);
				
				total = Integer.parseInt(runQuery(stmt,"select count(*) from alert_user_alert as t1, d3_user as t2\r\n" + 
                        "where t1.user_id = t2.id and t2.host_id='"+usrUid.get(0)+"'", false, true));
				Assert.assertEquals("Expected total number of alerts in the DB is incorrect!", expectedNumofAlerts, total);
				
				total = Integer.parseInt(runQuery(stmt,"select count(*) from user_message as t1, d3_user as t2\r\n" + 
                        "where t1.user_id = t2.id and t2.host_id='"+usrUid.get(0)+"'", false, true));
				Assert.assertEquals("Expected total number of messages in the DB is incorrect!", expectedNumOfMsgs, total);
				
				total = Integer.parseInt(runQuery(stmt,"select count(*) from category as t1, d3_user as t2\r\n" + 
                        "where t1.user_id = t2.id and t2.host_id='"+usrUid.get(0)+"'", false, true));
				Assert.assertEquals("Expected total number of categories in the DB is incorrect!", expectedNumOfCats, total);
				
				total = Integer.parseInt(runQuery(stmt,"select count(*) from user_transaction_split as t1, d3_user as t2\r\n" + 
                        "where t1.user_id = t2.id and t2.host_id='"+usrUid.get(0)+"'", false, true));
				Assert.assertEquals("Expected total number of tnx splits in the DB is incorrect!", expectedNumOfTnxSplits, total);
				
				total = Integer.parseInt(runQuery(stmt,"select count(*) from alert_user_dest as t1, d3_user as t2, alert_user_dest_join as t3\r\n" + 
						"  where t1.user_id = t2.id and t2.host_id='"+usrUid.get(0)+"' and t3.user_dest_id=t1.id", false, true));
				Assert.assertEquals("Expected total number of alert_user_dest_join in the DB is incorrect!", expectedNumAlertDestJoin, total);
				
				total = Integer.parseInt(runQuery(stmt,"select count(*) from alert_user_alert as t1, d3_user as t2, alert_user_alert_prop as t3\r\n" + 
						"  where t1.user_id = t2.id and t2.host_id='"+usrUid.get(0)+"' and t3.user_alert_id=t1.id", false, true));
				Assert.assertEquals("Expected total number of alert_user_alert_prop in the DB is incorrect!", expectedNumOfAlertProp, total);
				closeDBConnection(stmt);
			}
	/**
	 * Validate accountDisplayList data in the DB
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateAccDisplay (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck, valueInXML = null;	
		List<String> usrmigUIDs = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		List<String> acctdispUIDs = getElementValueInConduitXMLFile(conduitFile, "acctdisp", "", "uid");
		for (int u = 0; u <usrmigUIDs.size(); u++)
		{
			for (int a = 0; a <acctdispUIDs.size(); a++)
			{
				//validate excl
				elementToCheck = "Excluded";
				List<String> excl = getElementValueInConduitXMLFile(conduitFile, "acctdisp", "excl", "");
				switch(excl.get(a))
				{
					case "true": valueInXML = "1";
							 break;
					case "false": valueInXML = "0";
				 			 break;
				 	default: valueInXML = excl.get(a);
				 			 break;
				}
				query = "select excluded from [user_account] as t1, [d3_account] as t2, d3_user as t3\r\n" + 
						"where t1.user_id=t3.id and t2.source_account_id = '"+acctdispUIDs.get(a)+"' and \r\n" + 
						"t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate hdn
				elementToCheck = "Hidden";
				List<String> hdn = getElementValueInConduitXMLFile(conduitFile, "acctdisp", "hdn", "");
				switch(hdn.get(a))
				{
					case "true": valueInXML = "1";
							 break;
					case "false": valueInXML = "0";
				 			 break;
					default: valueInXML = hdn.get(a);
		 			         break;
				}
				query = "select hidden from [user_account] as t1, [d3_account] as t2, d3_user as t3\r\n" + 
						"where t1.user_id=t3.id and t2.source_account_id = '"+acctdispUIDs.get(a)+"' and \r\n" + 
						"t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate hba
				elementToCheck = "HiddenByAdmin";
				List<String> hba = getElementValueInConduitXMLFile(conduitFile, "acctdisp", "hba", "");
				switch(hba.get(a))
				{
					case "true": valueInXML = "1";
							 break;
					case "false": valueInXML = "0";
				 			 break;
					default: valueInXML = hba.get(a);
		 			         break;
				}
				query = "select hidden_by_admin from [user_account] as t1, [d3_account] as t2, d3_user as t3\r\n" + 
						"where t1.user_id=t3.id and t2.source_account_id = '"+acctdispUIDs.get(a)+"' and \r\n" + 
						"t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  valueInXML);
				//validate do
				elementToCheck = "DisplayOrder";
				List<String> dispOrder = getElementValueInConduitXMLFile(conduitFile, "acctdisp", "do", "");
				query = "select display_order from [user_account] as t1, [d3_account] as t2, d3_user as t3\r\n" + 
						"where t1.user_id=t3.id and t2.source_account_id = '"+acctdispUIDs.get(a)+"' and \r\n" + 
						"t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  dispOrder.get(a));			
			}
		}
		closeDBConnection(_stmt);
	}

	public void validateCatMapping (String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
	  	List<Object> _stmt = connectToDB();
		String query, elementToCheck;	
		List<String> usrmigUIDs = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		List<String> catMapAuid = getElementValueInConduitXMLFile(conduitFile, "catmap", "", "auid");
		List<String> catMapTuid = getElementValueInConduitXMLFile(conduitFile, "catmap", "", "tuid");
		List<String> catTxnListCat = getElementValueInConduitXMLFile(conduitFile, "catmap", "cattxnlst", "cat", "", "uid");
		for (int u = 0; u <catMapTuid.size(); u++)
		{
			//validate mm/nm
			elementToCheck = "CatMapping Name";
			List<String> catMapMm = getElementValueInConduitXMLFile(conduitFile, "catmap", "mm", "");
			List<String> catMapName = getElementValueInConduitXMLFile(conduitFile, "catmap", "nm", "");
			query = "select t1.user_description from [user_transaction] as t1, [d3_transaction] as t2, d3_user as t3, [d3_account] as t4\r\n" + 
					"where t1.transaction_id=t2.id and t2.source_tx_id='"+catMapTuid.get(u)+"' and \r\n" + 
					"t1.user_id=t3.id and t3.host_id = '"+usrmigUIDs.get(u)+"'and t2.account_id=t4.id and t4.source_account_id = '"+catMapAuid.get(u)+"'\r\n" + 
					"and t1.user_memo = '"+catMapMm.get(u)+"'";
			compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  catMapName.get(u));
			//validate cattxnlist cat
			for (int a = 0; a <catTxnListCat.size(); a++)
			{
				elementToCheck = "SplitAmount";
				List<String> splitAmount = getElementValueInConduitXMLFile(conduitFile, "catmap", "cattxnlst", "cat", "amt", "");
				List<String> catName = getElementValueInConduitXMLFile(conduitFile, "usrmig", "catlst", "cat", "nm", "");
				query = "select t1.split_amount from user_transaction_split as t1, category as t2, [d3_transaction] as t3, [d3_account] as t4, d3_user as t5\r\n" + 
						"where t1.category_id=t2.id and t1.transaction_id=t3.id and t1.user_id=t5.id and t3.account_id = t4.id and\r\n" + 
						"t5.host_id = '"+usrmigUIDs.get(u)+"' and t4.source_account_id = '"+catMapAuid.get(u)+"' and t3.source_tx_id = '"+catMapTuid.get(u)+"' and t2.category_name = '"+catName.get(0)+"'\r\n" + 
						"and t1.split_amount = '"+splitAmount.get(a)+"'";
				compareDataInXMLWithDB(_stmt, query,  conduitFile, elementToCheck,  splitAmount.get(a));
			}
		}
		closeDBConnection(_stmt);
	}
	
	/**
	 * Check accounts' deletion flag
	 * @param conduitFile
	 * @param expectedNumOfAcctInDB
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void checkAccDeletionSet(String conduitFile, boolean areAccsExist) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		List<String> acctUIDs = getElementValueInConduitXMLFile(conduitFile, "acct", "", "uid");
		List<String> acctDels = getElementValueInConduitXMLFile(conduitFile, "acct", "", "del");
		for (int n=0; n<acctUIDs.size(); n++)
		{
			if (acctDels.get(n).toLowerCase().equals("true") || acctDels.get(n).toLowerCase().equals("1") || acctDels.get(n).toLowerCase().equals("t"))
			{
				if(areAccsExist)
				{
					Assert.assertEquals(acctUIDs.get(n) + " - account is not marketed as deleted in the db! Expecting, it should.", 
							1, 
							Integer.parseInt(runQuery(stmt,"select deleted from d3_account where source_account_id = '"+acctUIDs.get(n)+"'", false, true)));
				}else{
					Assert.assertEquals(acctUIDs.get(n) + " - account is not marketed as deleted in the db! Expecting, it should.", 
							0, 
							Integer.parseInt(runQuery(stmt,"select deleted from d3_account where source_account_id = '"+acctUIDs.get(n)+"'", false, true)));
				}
			}else{
				Assert.assertEquals(acctUIDs.get(n) + " - account is marketed as deleted in the db! Expecting, it should not.", 
						0, 
						Integer.parseInt(runQuery(stmt,"select deleted from d3_account where source_account_id = '"+acctUIDs.get(n)+"'", false, true)));
			}
		}
		
		closeDBConnection(stmt);
	}
	
	/***
	 * Validate transaction deletion
	 * @param conduitFile1
	 * @param conduitFile2
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	public void checkTxnsDeletion(String conduitFile1, String conduitFile2) throws ClassNotFoundException, 
	ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException, ParseException 
	{
		List<Object> stmt = connectToDB();
		SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTo = new SimpleDateFormat("dd/MM/yyyy");
		Date bdt = new SimpleDateFormat("dd/MM/yyyy").parse(formatTo.format(formatFrom.parse(getElementValueInConduitXMLFile(conduitFile2, "txndtrng", "", "bdt").get(0))));
		Date edt = new SimpleDateFormat("dd/MM/yyyy").parse(formatTo.format(formatFrom.parse(getElementValueInConduitXMLFile(conduitFile2, "txndtrng", "", "edt").get(0))));
		List<String> tnxUids2 = getElementValueInConduitXMLFile(conduitFile2, "txn", "", "uid");
		List<String> tnxUids1 = getElementValueInConduitXMLFile(conduitFile1, "txn", "", "uid");
		List<String> tnxPendingStatus = getElementValueInConduitXMLFile(conduitFile1, "txn", "", "pn");
		List<String> tnxPostDates = getElementValueInConduitXMLFile(conduitFile1, "txn", "", "pd");
		String tnxsInFile2= "";
		for (int l=0; l<tnxUids2.size(); l++)
		{
			tnxsInFile2 = tnxsInFile2 + "|" + tnxUids2.get(l);			
		}
		for (int n=0; n<tnxUids1.size(); n++)
		{
			 Date tranPostDate = new SimpleDateFormat("dd/MM/yyyy").parse(formatTo.format(formatFrom.parse(tnxPostDates.get(n))));
			  if (tranPostDate.after(bdt) && tranPostDate.before(edt) || tranPostDate.equals(bdt) || tranPostDate.equals(edt))
			  {
				if (Integer.parseInt(tnxPendingStatus.get(n))==1)
				{
					if(!tnxsInFile2.contains(tnxUids1.get(n)))
					{
						Assert.assertEquals(tnxUids1.get(n) + " - transaction has not been deleted! Expecting, it should.", 
								0, 
								Integer.parseInt(runQuery(stmt,"select count(*) from d3_transaction where source_tx_id = '"+tnxUids1.get(n)+"'", false, true)));
						System.out.println(tnxUids1.get(n) + " transaction deletion checked!");
					}
				}
			  }
		}
		
		closeDBConnection(stmt);
	}
	
	/**
	 * Validate user deletion flag
	 * @param conduitFile
	 * @param areUsersExist
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void checkUserDeletionSet(String conduitFile, boolean areUsersExist) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		List<String> usrUIDs = getElementValueInConduitXMLFile(conduitFile, "usr", "", "uid");
		List<String> usrDels = getElementValueInConduitXMLFile(conduitFile, "usr", "", "del");
		for (int n=0; n<usrUIDs.size(); n++)
		{
			if (usrDels.get(n).toLowerCase().equals("true") || usrDels.get(n).toLowerCase().equals("1") || usrDels.get(n).toLowerCase().equals("t"))
			{
				if(areUsersExist)
				{
					Assert.assertEquals(usrUIDs.get(n) + " - user is not marketed as deleted in the db! Expecting, it should.", 
							1, 
							Integer.parseInt(runQuery(stmt,"select deleted from d3_user where host_id = '"+usrUIDs.get(n)+"'", false, true)));
				}else{
					Assert.assertEquals(usrUIDs.get(n) + " - user is not marketed as deleted in the db! Expecting, it should.", 
							0, 
							Integer.parseInt(runQuery(stmt,"select deleted from d3_user where host_id = '"+usrUIDs.get(n)+"'", false, true)));
				}
			}else{
				Assert.assertEquals(usrUIDs.get(n) + " - user is marketed as deleted in the db! Expecting, it should not.", 
						0, 
						Integer.parseInt(runQuery(stmt,"select deleted from d3_user where host_id = '"+usrUIDs.get(n)+"'", false, true)));
			}
		}
		
		closeDBConnection(stmt);
	 }
	/**
	 * Validate accountList element values nullify
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void checkAccountListElementsNullify(String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
	  	List<Object> stmt = connectToDB();
		List<String> valuesInXML = new ArrayList<String>();
		//List<String> acctUIDs = getElementValueInConduitXMLFile(conduitFile, "acct", "", "uid");
		List<String> txnUIDs = getElementValueInConduitXMLFile(conduitFile, "txn", "", "uid");
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "acct", "txnlst", "mm", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("", 
						"", 
						runQuery(stmt,"  SELECT memo FROM d3_transaction where source_tx_id = '"+txnUIDs.get(u)+"'", false, true));
			}
		}
		closeDBConnection(stmt);
	 }
	
	public void checkUserListElementsNullify(String conduitFile)
	{
	  	
	}
	
	/**
	 * Validate company soft deletion
	 * @param conduitFile
	 * @param areCompsExist
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void checkCompanyListDeletionSet(String conduitFile, boolean areCompsExist) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		List<String> compUIDs = getElementValueInConduitXMLFile(conduitFile, "comp", "", "uid");
		List<String> compDels = getElementValueInConduitXMLFile(conduitFile, "comp", "", "del");
		for (int n=0; n<compUIDs.size(); n++)
		{
			if (compDels.get(n).toLowerCase().equals("true") || compDels.get(n).toLowerCase().equals("1") || compDels.get(n).toLowerCase().equals("t"))
			{
				if(areCompsExist)
				{
					Assert.assertEquals(compUIDs.get(n) + " - company is not marketed as deleted in the db! Expecting, it should.", 
							1, 
							Integer.parseInt(runQuery(stmt,"select deleted from company where source_company_id = '"+compUIDs.get(n)+"'", false, true)));
				}else{
					Assert.assertEquals(compUIDs.get(n) + " - company is not marketed as deleted in the db! Expecting, it should.", 
							0, 
							Integer.parseInt(runQuery(stmt,"select deleted from company where source_company_id = '"+compUIDs.get(n)+"'", false, true)));
				}
			}else{
				Assert.assertEquals(compUIDs.get(n) + " - company is marketed as deleted in the db! Expecting, it should not.", 
						0, 
						Integer.parseInt(runQuery(stmt,"select deleted from company where source_company_id = '"+compUIDs.get(n)+"'", false, true)));
			}
		}
		
		closeDBConnection(stmt);
	}
	/**
	 * Validate account product soft deletion
	 * @param conduitFile
	 * @param areAccProdsExist
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void checkAccProdListDeletionSet(String conduitFile, boolean areAccProdsExist) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		List<String> accProdUIDs = getElementValueInConduitXMLFile(conduitFile, "acctprod", "", "uid");
		List<String> accProdDels = getElementValueInConduitXMLFile(conduitFile, "acctprod", "", "del");
		for (int n=0; n<accProdUIDs.size(); n++)
		{
			if (accProdDels.get(n).toLowerCase().equals("true") || accProdDels.get(n).toLowerCase().equals("1") || accProdDels.get(n).toLowerCase().equals("t"))
			{
				if(areAccProdsExist)
				{
					Assert.assertEquals(accProdUIDs.get(n) + " - account_product is not marketed as deleted in the db! Expecting, it should.", 
							1, 
							Integer.parseInt(runQuery(stmt,"select deleted from account_product where source_product_id = '"+accProdUIDs.get(n)+"'", false, true)));
				}else{
					Assert.assertEquals(accProdUIDs.get(n) + " - account_product is not marketed as deleted in the db! Expecting, it should.", 
							0, 
							Integer.parseInt(runQuery(stmt,"select deleted from account_product where source_product_id = '"+accProdUIDs.get(n)+"'", false, true)));
				}
			}else{
				Assert.assertEquals(accProdUIDs.get(n) + " - account_product is marketed as deleted in the db! Expecting, it should not.", 
						0, 
						Integer.parseInt(runQuery(stmt,"select deleted from account_product where source_product_id = '"+accProdUIDs.get(n)+"'", false, true)));
			}
		}
		
		closeDBConnection(stmt);
	}
	
	public void checkCompElementsNullify(String conduitFile) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
		List<Object> stmt = connectToDB();
		List<String> valuesInXML = new ArrayList<String>();
		List<String> busNames = getElementValueInConduitXMLFile(conduitFile, "busn", "nm", "");
		//first_name
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "fn", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("first_name is not nullified!", 
						"", 
						runQuery(stmt,"select t2.first_name from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//middle_name
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "mn", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("middle_name is not nullified!", 
						"", 
						runQuery(stmt,"select t2.middle_name from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//alternate_email
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "aem", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("alternate_email is not nullified!", 
						"", 
						runQuery(stmt,"select t2.alternate_email from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}	
		//primary_email
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "pem", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("primary_email is not nullified!", 
						"", 
						runQuery(stmt,"select t2.primary_email from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//home_phone
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "hph", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("home_phone is not nullified!", 
						"", 
						runQuery(stmt,"select t2.home_phone from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//work_phone
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "wph", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("work_phone is not nullified!", 
						"", 
						runQuery(stmt,"select t2.work_phone from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//mobile_phone
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "mph", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("mobile_phone is not nullified!", 
						"", 
						runQuery(stmt,"select t2.mobile_phone from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//employee
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct1", "emp", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("employee is not nullified!", 
						"0", 
						runQuery(stmt,"select t2.employee from [business] as t1, person as t2 where t1.contact1_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		
		//alternate_email
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct2", "aem", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("alternate_email is not nullified!", 
						"", 
						runQuery(stmt,"select t2.alternate_email from [business] as t1, person as t2 where t1.contact2_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}	
		//primary_email
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct2", "pem", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("primary_email is not nullified!", 
						"", 
						runQuery(stmt,"select t2.primary_email from [business] as t1, person as t2 where t1.contact2_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//home_phone
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct2", "hph", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("home_phone is not nullified!", 
						"", 
						runQuery(stmt,"select t2.home_phone from [business] as t1, person as t2 where t1.contact2_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//work_phone
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct2", "wph", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("work_phone is not nullified!", 
						"", 
						runQuery(stmt,"select t2.work_phone from [business] as t1, person as t2 where t1.contact2_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//mobile_phone
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct2", "mph", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("mobile_phone is not nullified!", 
						"", 
						runQuery(stmt,"select t2.mobile_phone from [business] as t1, person as t2 where t1.contact2_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		//employee
		valuesInXML = getElementValueInConduitXMLFile(conduitFile, "busn", "ct2", "emp", "", "del");
		for (int u = 0; u <valuesInXML.size(); u++)
		{
			if (valuesInXML.get(u).toLowerCase().equals("true") || valuesInXML.get(u).toLowerCase().equals("1") || valuesInXML.get(u).toLowerCase().equals("t"))
			{
				Assert.assertEquals("employee is not nullified!", 
						"1", 
						runQuery(stmt,"select t2.employee from [business] as t1, person as t2 where t1.contact2_id=t2.id and t1.name = '"+busNames.get(u)+"'", false, true));
			}
		}
		closeDBConnection(stmt);
	}
	
	/**
	 * To validate stored data into DB after processing file with scheduled transfers
	 * @param conduitFile
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void validateScheduledTransfers(String conduitFile, int numOfReccurring, int numOfOneTime) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		List<String> valuesInXML = new ArrayList<String>();
		String query = "";
		String valueInXML = "";
		List<String> userUid = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		for (int u = 0; u <userUid.size(); u++)
		{
			//fauid
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "scxfr", "fauid", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from transfer as t1, [d3_account] t2, d3_user as t3, [user_account] as t4\r\n" + 
						"  where t1.from_account_id=t4.id and t2.id=t4.account_id and t1.user_id=t3.id and t2.source_account_id = '"+valuesInXML.get(t)+"' and t3.host_id = '"+userUid.get(u)+"'";
				Assert.assertEquals("fauid value is not stored in the db! Query run: " + query, 
						Integer.toString(numOfReccurring+numOfOneTime), 
						runQuery(stmt, query, false, true));
			}		
			//tauid
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "scxfr", "tauid", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from [internal_account_recipient] as t1, [d3_account] as t2, d3_user t3, [user_account] t4\r\n" + 
						"  where t1.user_account_id=t4.id and t4.account_id=t2.id and t4.user_id=t3.id and \r\n" + 
						"  t3.host_id = '"+userUid.get(u)+"' and t2.source_account_id='"+valuesInXML.get(t)+"'";
				Assert.assertEquals("scxfr/fauid value is not stored in the db! Query run: " + query, 
						"1",  
						runQuery(stmt, query, false, true));
			}
			//mm
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "scxfr", "mm", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from [recurring_model] as t1, d3_user t2\r\n" + 
						"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"'";
				Assert.assertEquals("scxfr/mm value is not stored in the db! Query run: " + query, 
						Integer.toString(numOfReccurring),   
						runQuery(stmt, query, false, true));				
			}
			//nm
			valuesInXML = getElementValueInConduitXMLFile(conduitFile,"scxfr", "nm", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from internal_transfer as t1, recipient as t2, d3_user t3\r\n" + 
						"  where t1.recipient_id=t2.id and t2.user_id=t3.id and t3.host_id = '"+userUid.get(u)+"' and t1.note = '"+valuesInXML.get(t)+"'";
				Assert.assertEquals("scxfr/nm value is not stored in the db! Query run: " + query, 
						"1",  
						runQuery(stmt, query, false, true));
			}
			//freq
			valuesInXML = getElementValueInConduitXMLFile(conduitFile,"scxfr", "freq", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{	
				if (!valuesInXML.get(t).equals("onetime"))
				{
					switch (valuesInXML.get(t)) {
			        case "every2weeks":
			        	valueInXML = "EVERY_TWO_WEEKS";
			        case "everyweek":
			        	valueInXML = "EVERY_WEEK";
			        case "every2months":
			        	valueInXML = "EVERY_TWO_MONTHS";
			        case "lastdom":
			        	valueInXML = "LAST_DAY_OF_MONTH";
			        case "everymonth":
			        	valueInXML = "EVERY_MONTH";
			        case "every3months":
			        	valueInXML = "EVERY_THREE_MONTHS";
			        case "every6months":
			        	valueInXML = "EVERY_SIX_MONTHS";
			        case "everyyear":
			        	valueInXML = "EVERY_YEAR";
			        }
					query = "select count(*) from internal_recurring_model as t1, recipient as t2, d3_user t3\r\n" + 
							"  where t1.recipient_id=t2.id and t2.user_id=t3.id and t3.host_id = '"+userUid.get(u)+"' and t1.frequency = '"+valueInXML+"'";
					Assert.assertEquals("scxfr/freq value is not stored in the db! Query run: " + query, 
							"3",  
							runQuery(stmt, query, false, true));
				}
			}
			//amt
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "scxfr", "amt", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				valueInXML = valuesInXML.get(t).substring(0, (valuesInXML.get(t).indexOf(".")+3));
				query = "select count(t1.amount) from transfer as t1, d3_user as t2\r\n" + 
						"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"' and t1.amount = '"+valueInXML+"'";
				Assert.assertEquals("scxfr/amt value is not stored in the db! Query run: " + query, 
						Integer.toString(numOfReccurring+numOfOneTime),  
						runQuery(stmt, query, false, true));
			}
			//bdt
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "scxfr", "bdt", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(t1.scheduled_date) from transfer as t1, d3_user as t2\r\n" + 
						"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"' and t1.scheduled_date = '"+valuesInXML.get(t)+"'";
				Assert.assertEquals("scxfr/bdt value is not stored in the db! Query run: " + query, 
						Integer.toString(numOfReccurring+numOfOneTime),  
						runQuery(stmt, query, false, true));
			}
			//edt
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "scxfr", "edt", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				if (!valuesInXML.get(t).equals("elementDoesNotExist"))
				{
					query = "select count(*) from recurring_model as t1, d3_user as t2\r\n" + 
							"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"' and t1.end_date = '"+valuesInXML.get(t)+"'";
					Assert.assertEquals("scxfr/edt value is not stored in the db! Query run: " + query, 
							"1",  
							runQuery(stmt, query, false, true));
				}
			}
			//alst
			List<String> n = getElementValueInConduitXMLFile(conduitFile, "scxfr", "alst", "a", "", "n");
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "scxfr", "a", "");
			for (int t = 0; t <n.size(); t++)
			{
				if(n.get(t).equals("indefinite"))
				{
					query = "select count(*) from [recurring_model] as t1, d3_user as t2\r\n" + 
							"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"' and t1.indefinite = '"+valuesInXML.get(t)+"'";
					Assert.assertEquals("scxfr/indefinite value is not stored in the db! Query run: " + query, 
							Integer.toString(numOfReccurring/3), 
							runQuery(stmt, query, false, true));
					query = "select count(*) from [internal_recurring_model] as t1, recipient as t2, d3_user as t3\r\n" + 
							"  where t1.recipient_id=t2.id and t2.user_id=t3.id and t3.host_id = '"+userUid.get(u)+"' and t1.indefinite = '"+valuesInXML.get(t)+"'";
					Assert.assertEquals("scxfr/indefinite value is not stored in the db! Query run: " + query, 
							Integer.toString(numOfReccurring/3),
							runQuery(stmt, query, false, true));
				}else if(n.get(t).equals("occurrences"))
				{
					query = "select count(*) from [recurring_model] as t1, d3_user as t2\r\n" + 
							"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"' and t1.occurrences = '"+valuesInXML.get(t)+"'";
					Assert.assertEquals("scxfr/occurrences value is not stored in the db! Query run: " + query, 
							"1",
							runQuery(stmt, query, false, true));
					query = "select count(*) from [internal_recurring_model] as t1, recipient as t2, d3_user as t3\r\n" + 
							"  where t1.recipient_id=t2.id and t2.user_id=t3.id and t3.host_id = '"+userUid.get(u)+"' and t1.occurrences = '"+valuesInXML.get(t)+"'";
					Assert.assertEquals("scxfr/occurrences value is not stored in the db! Query run: " + query, 
							"1",
							runQuery(stmt, query, false, true));
				}
			}

		}
		closeDBConnection(stmt);
	}
	
	public void validateTransfers(String conduitFile, int numOfTransfers) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException 
	{
		List<Object> stmt = connectToDB();
		List<String> valuesInXML = new ArrayList<String>();
		String query = "";
		String valueInXML = "";
		List<String> userUid = getElementValueInConduitXMLFile(conduitFile, "usrmig", "", "uid");
		for (int u = 0; u <userUid.size(); u++)
		{
			//fauid
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "fauid", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from transfer as t1, d3_account t2, d3_user as t3, user_account as t4\r\n" + 
						"  where t1.from_account_id=t4.id and t2.id=t4.account_id and t1.user_id=t3.id and t2.source_account_id = '"+valuesInXML.get(t)+"' and t3.host_id = '"+userUid.get(u)+"'";
				Assert.assertEquals("fauid value is not stored in the db! Query run: " + query, 
						Integer.toString(numOfTransfers), 
						runQuery(stmt, query, false, true));
			}		
			//tauid
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "tauid", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from [internal_account_recipient] as t1, [d3_account] as t2, d3_user t3, [user_account] t4\r\n" + 
						"  where t1.user_account_id=t4.id and t4.account_id=t2.id and t4.user_id=t3.id and \r\n" + 
						"  t3.host_id = '"+userUid.get(u)+"' and t2.source_account_id='"+valuesInXML.get(t)+"'";
				Assert.assertEquals("scxfr/fauid value is not stored in the db! Query run: " + query, 
						"1",
						runQuery(stmt, query, false, true));
			}
			/*//mm
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "mm", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from [recurring_model] as t1, d3_user t2\r\n" + 
						"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"'";
				Assert.assertEquals("scxfr/mm value is not stored in the db! Query run: " + query, 
						"1",   
						runQuery(stmt, query, false, true));				
			}*/
			//nm
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "nm", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(*) from internal_transfer as t1, recipient as t2, d3_user t3\r\n" + 
						"  where t1.recipient_id=t2.id and t2.user_id=t3.id and t3.host_id = '"+userUid.get(u)+"' and t1.note = '"+valuesInXML.get(t)+"'";
				Assert.assertEquals("scxfr/nm value is not stored in the db! Query run: " + query, 
						"1",  
						runQuery(stmt, query, false, true));
			}
			//amt
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "amt", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				valueInXML = valuesInXML.get(t).substring(0, (valuesInXML.get(t).indexOf(".")+3));
				query = "select count(t1.amount) from transfer as t1, d3_user as t2\r\n" + 
						"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"' and t1.amount = '"+valueInXML+"'";
				Assert.assertEquals("scxfr/amt value is not stored in the db! Query run: " + query, 
						"1",  
						runQuery(stmt, query, false, true));
			}
			//postdt
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "postdt", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				query = "select count(t1.scheduled_date) from transfer as t1, d3_user as t2\r\n" + 
						"  where t1.user_id=t2.id and t2.host_id = '"+userUid.get(u)+"' and t1.scheduled_date = '"+valuesInXML.get(t)+"'";
				Assert.assertEquals("scxfr/bdt value is not stored in the db! Query run: " + query, 
						"1",  
						runQuery(stmt, query, false, true));
			}
			//postsq
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "postsq", "");
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				if(!valuesInXML.get(t).equals("elementDoesNotExist"))
				{
					query = "select count(*) from internal_transfer as t1, recipient as t2, d3_user t3\r\n" + 
							"  where t1.recipient_id=t2.id and t2.user_id=t3.id and t3.host_id = '"+userUid.get(u)+"' and t1.posting_seq = '"+valuesInXML.get(t)+"'";
					Assert.assertEquals("scxfr/postsq value is not stored in the db! Query run: " + query, 
							"1",  
							runQuery(stmt, query, false, true));
				}
			}
			//cnfmnbr
			valuesInXML = getElementValueInConduitXMLFile(conduitFile, "xfr", "cnfmnbr", "");
			
			for (int t = 0; t <valuesInXML.size(); t++)
			{
				if(!valuesInXML.get(t).equals("elementDoesNotExist"))
				{
					query = "select count(*) from internal_transfer as t1, recipient as t2, d3_user t3\r\n" + 
							"  where t1.recipient_id=t2.id and t2.user_id=t3.id and t3.host_id = '"+userUid.get(u)+"' and t1.confirmation_number = '"+valuesInXML.get(t)+"'";
					Assert.assertEquals("scxfr/cnfmnbr value is not stored in the db! Query run: " + query, 
							"1",  
							runQuery(stmt, query, false, true));
				}	
			}
		}
		closeDBConnection(stmt);
	}
	
	/**
	 * Get today's date
	 * @return
	 */
	public String getTodayDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("ss-mm-dd-M-yy-hh");
        return sdf.format(new Date());		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
