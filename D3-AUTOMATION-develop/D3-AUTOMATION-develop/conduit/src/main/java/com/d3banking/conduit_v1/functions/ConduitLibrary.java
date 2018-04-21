package com.d3banking.conduit_v1.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.ComparisonFailure;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v2.functions.ReadXMLData;


public class ConduitLibrary {
	
	ReadXMLData readXMLData = new ReadXMLData();
	String _tomcatPath = "C:\\apps\\apache-tomcat-7.0.26\\bin\\";
	String _folderProcessedData = "processedData";
	String _folderErrorData = "errorData";
	String _folderFailedData = "failedData";
	String _folderPending = "pending";
	String _folderDuplicate = "duplicateData";
	
    /**
     * Drop the conduit test file in the incomingData folder
     */
   public void dropConduitTestFile(String _sourceFile, String _destinationPath)
    {
		try
		{
			String sourcePath = getClass().getResource("/" + _sourceFile).toString().replace("file:/", "");
			String tempPath = getClass().getResource("/" + _sourceFile).toString().replace("file:/", "").replace("-test", "");
			File source = new File(sourcePath);
			File tempDest = new File(tempPath);
			InputStream in = new FileInputStream(source);
			File dest = new File(_destinationPath, source.getName());
			OutputStream out = new FileOutputStream(tempDest);
			byte[] buf = new byte[10000];
			  int len;
			  while ((len = in.read(buf)) > 0){
			  out.write(buf, 0, len);
			  }
			  in.close();
			  out.close();
			  tempDest.renameTo(dest);
			  System.out.println( "Conduit file copied to \"" + dest.getCanonicalPath() + "\"." );
		}
		catch(FileNotFoundException ex)
		{
		  System.out.println(ex.getMessage() + " in the specified directory.");
		  System.exit(0);
		}
		catch(IOException e)
		{
		  System.out.println(e.getMessage());  
		}		
    }
	
   /**
    * Wait until Conduit test files processing finishes
 * @throws InterruptedException 
    */
	
  public void waitConduitFileProcessing(String _sourceFile, String _destinationPath, int _waitSecs) throws InterruptedException
   {
		File file = new File(_destinationPath, _sourceFile);
		Calendar now = Calendar.getInstance();
		int secs = now.get(Calendar.MINUTE) *60 + now.get(Calendar.SECOND);
		while((secs + _waitSecs) > now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND))
		{
			if(!file.exists())
			{
				break;
			}
			now = Calendar.getInstance();
		}
		if(file.exists())
		{
			int attempt = 0;
			while(file.exists())
			{
				file.delete();
				if (attempt>20){break;}
			}
			Assert.fail("Waited "+_sourceFile+" file processing for " + _waitSecs +
					" secs. The file is not moved into processedData folder. Deleting file & existing test!");
		}
		Thread.sleep(8000); //perf issue
   }
  
  public void waitConduitFileProcessing(String _sourceFile, String _destinationPath, int _waitSecs, boolean _perfTest) throws InterruptedException
  {
		File file = new File(_destinationPath, _sourceFile);
		Calendar now = Calendar.getInstance();
		int secs = now.get(Calendar.MINUTE) *60 + now.get(Calendar.SECOND);
		while((secs + _waitSecs) > now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND))
		{
			if(!file.exists())
			{
				break;
			}
			now = Calendar.getInstance();
		}
		if(file.exists())
		{
			int attempt = 0;
			while(file.exists())
			{
				file.delete();
				if (attempt>20){break;}
			}
			Assert.fail("Waited "+_sourceFile+" file processing for " + _waitSecs +
					" secs. The file is not moved into processedData folder. Deleting file & existing test!");
		}
  }
  
  /**
   * Wait until Conduit creates/updates data in the DB
* @throws InterruptedException 
 * @throws SQLException 
   */
	
 public void waitConduitBranchProcess(List<Object> _stmt, String _branchExternalID, int _waitTime) throws InterruptedException, SQLException
  {
	 int _loop = 1;
	 ResultSet rs=null;
	 while (_loop > 0)
	 {
			Statement stmt = (Statement) _stmt.get(0);		
		  	rs = ((Statement) stmt).executeQuery("SELECT EXTERNAL_ID from [BRANCH] where EXTERNAL_ID = '"+_branchExternalID+"'");
		  	rs.next();
	  		try{
	  			rs.getString(1);
	  			break;
	  			}
	  		catch(SQLException e)
	  		{
			  	rs = ((Statement) stmt).executeQuery("SELECT * from [SYSTEM_EVENT]");
			  	rs.next();
		  		try{
		  			rs.getString(1);
		  			break;
		  			}
		  		catch(SQLException e2)
		  		{

		  			} 
	  			} 
	  		_loop = _loop + 1;
	  		Thread.sleep(2000);
	  		if (_loop==_waitTime) {break;}
	 }
  }
 
 public void waitConduitBranchProcess(String _fileName, int _waitSecs) throws InterruptedException, SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException
 {
	 List<Object> _stmt = connectToDB();
	 List<String> _branchExternalID = getElementValueInConduitXMLFile(_fileName, "branch", "", "externalId");
	 ResultSet rs=null;
	 Calendar now = Calendar.getInstance();
	 int secs = now.get(Calendar.MINUTE) *60 + now.get(Calendar.SECOND);
	 while((secs + _waitSecs) > (now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND))) 
	 {
			Statement stmt = (Statement) _stmt.get(0);		
		  	rs = ((Statement) stmt).executeQuery("SELECT EXTERNAL_ID from [BRANCH] where EXTERNAL_ID = '"+_branchExternalID.get(0)+"'");
		  	rs.next();
	  		try{
	  			rs.getString(1);
	  			break;
	  			}
	  		catch(SQLException e)
	  		{
	  		} 
	 }
 }

  /**
   * Read data in XML file
   * @throws IOException
   * @throws SAXException
   */
  public List<String> getElementValueInConduitXMLFile(String _fileName, String _parentElementTagName, String _subElementTagName, String _attTagName) throws ParserConfigurationException, SAXException, IOException
  {
		Class<? extends ConduitLibrary> thisClass = getClass();
		URL thisResource = thisClass.getResource( "/" + _fileName );
		String _filePath = thisResource .toString().replace("file:/", "");
		File fXmlFile = new File(_filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName(_parentElementTagName);
		List<String> dataInXML = new ArrayList<String>();
		if (nList.getLength()==0){dataInXML.add("elementDoesNotExist");}
		for (int n = 0; n < nList.getLength(); n++) {
			   Node nNode = nList.item(n);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			      Element eElement = (Element) nNode;
			      if (!_attTagName.equals(""))
			      {  	  
			    	  if (eElement.getAttribute(_attTagName).equals(""))
			    	  {
			    		  dataInXML.add("elementDoesNotExist");
			    	  }else{
			    		  dataInXML.add(eElement.getAttribute(_attTagName).trim());
			    	  }
			      }
			      if (!_subElementTagName.equals(""))
			      {
			  		try {
			  			eElement.getElementsByTagName(_subElementTagName).item(0).getChildNodes().item(0);
				  		try {
				  			dataInXML.add(eElement.getElementsByTagName(_subElementTagName).item(0).getChildNodes().item(0).getNodeValue().trim());
						} catch (NullPointerException e1) {
							dataInXML.add("EmptyValue");
					    }	
					} catch (NullPointerException e1) {
						dataInXML.add("elementDoesNotExist");
				    }						
				  }
			   }
		}
		return dataInXML;
  }
  
  /**
   * RE-write data in XML file
   * @throws IOException
   * @throws SAXException
 * @throws TransformerFactoryConfigurationError 
 * @throws TransformerException 
   */
  public void setElementValueInConduitXMLFile(String _fileName, String _parentElementTagName, String _subElementTagName, String _attTagName,
		  String _value) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
  {
	  String _filePath = getClass().getResource("/"+_fileName).toString().replace("file:/", "");
      File fXmlFile = new File(_filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName(_parentElementTagName);
		for (int n = 0; n < nList.getLength(); n++) {
			   Node nNode = nList.item(n);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			      Element eElement = (Element) nNode;
			      if (!_attTagName.equals(""))
			      {
			    	  eElement.setAttribute(_attTagName, _value);
			      }
			      if (!_subElementTagName.equals(""))
			      {
			  		try {
			  			eElement.getElementsByTagName(_subElementTagName).item(0).getChildNodes().item(0).setNodeValue(_value);
					} catch (NullPointerException e) {
					}
			      }
			   }
		}
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		Result dest = new StreamResult(new FileWriter (_filePath));
		transformer.transform(source, dest);
  }
  public void setElementValueInConduitXMLFile(String _fileName, String _parentElementTagName, String _subElementTagName, String _attTagName,
		  String _value, int _index) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
  {
	  String _filePath = getClass().getResource("/"+_fileName).toString().replace("file:/", "");
      File fXmlFile = new File(_filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName(_parentElementTagName);
	   Node nNode = nList.item(_index);
	   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	      Element eElement = (Element) nNode;
	  		try {
	  			eElement.getElementsByTagName(_subElementTagName).item(0).getChildNodes().item(0).setNodeValue(_value);
			} catch (NullPointerException e) {
			}

	   }
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		Result dest = new StreamResult(new FileWriter (_filePath));
		transformer.transform(source, dest);
  }
  
  /**
   * Connect to DB
 * @throws ClassNotFoundException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws InterruptedException 
   */
  public List<Object> connectToDB() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
	{
	  	Connection conn=null;
		Statement stmt = null;
		List<Object> _sqlObjects = new ArrayList<Object>();
		String url = "jdbc:jtds:sqlserver://"+readXMLData.getTestDataFor("urlDB").get(0)+";DatabaseName="+readXMLData.getTestDataFor("dbName").get(0)+"";
		String driverName = "net.sourceforge.jtds.jdbc.Driver";
		Class.forName(driverName);
		String userDB = readXMLData.getTestDataFor("userDB").get(0);
		String passwordDB = readXMLData.getTestDataFor("passwordDB").get(0);
		System.out.println( "connectToDB - trying to log in as user \"" + userDB + "\"" );
		conn = DriverManager.getConnection( url, userDB, passwordDB );
		stmt = conn.createStatement();
		_sqlObjects.add(stmt);
		_sqlObjects.add(conn);
		Thread.sleep(1000);
		return _sqlObjects;
	}
  
  /**
   * Connect to DB
 * @throws ClassNotFoundException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws InterruptedException 
   */
  public void closeDBConnection(List<Object> _sqlObjects) throws SQLException
	{
	  Connection conn=(Connection) _sqlObjects.get(1);	
	  conn.close(); 
	}
  
  /**
   * Validate Branch Data
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws InterruptedException 
   */
  public void validateConduitBranch(String _createFile, String _updateFile, boolean _isCreateProcessShouldFail,  
		  boolean _isThisUpdate, boolean _isUpdateProcessShouldFail) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
	{
	  	List<Object> _stmt = connectToDB();
	  	ResultSet rs=null;
		String queryStr1, queryStr2,_fieldName;
		String _fileName = _createFile;	
		if(_isThisUpdate){if(!_isUpdateProcessShouldFail){_fileName = _updateFile;}}
		List<String> _branchExternalID = getElementValueInConduitXMLFile(_fileName, "branch", "", "externalId");
		waitConduitBranchProcess(_stmt, _branchExternalID.get(0), 20);
		for (int u = 0; u <_branchExternalID.size(); u++)
		{
			if (!_isCreateProcessShouldFail)
				{
					//validate _branchExternalID values
					queryStr1 = "SELECT ";
					queryStr2 = " from [BRANCH] where EXTERNAL_ID = '"+_branchExternalID.get(u)+"'";
					_fieldName = "EXTERNAL_ID";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "branch", "", "externalId");
					//validate parentExternalId values
					queryStr1 = "SELECT t1.";
					queryStr2 = " from [COMPANY_STRUCTURE] as t1, [BRANCH] as t2 where t1.ID=t2.COMPANY_ID and t2.EXTERNAL_ID = '"+_branchExternalID.get(u)+"'";
					_fieldName = "EXTERNAL_ID";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "branch", "", "parentExternalId");
					//validate _businessName values
					queryStr1 = "SELECT t2.";
					queryStr2 = " from [BRANCH] as t1, [BUSINESS] as t2 where t1.BUSINESS_ID=t2.ID and t1.EXTERNAL_ID = '"+_branchExternalID.get(u)+"'";
					_fieldName = "[NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "business", "name", "");
					//validate _businessPhone values
					_fieldName = "[PHONE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "business", "phone", "");
					//validate _businessPhysAddress1 values
					queryStr1 = "SELECT t3.";
					queryStr2 = " from [BRANCH] as t1, [BUSINESS] as t2, [ADDRESS] as t3 where t1.BUSINESS_ID=t2.ID and"+
								" t2.PHYSICAL_ADDRESS_ID=t3.ID and t1.EXTERNAL_ID = '"+_branchExternalID.get(u)+"'";
					_fieldName = "[ADDRESS1]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "address1", "");
					//validate _businessPhysAddress2 values
					_fieldName = "[ADDRESS2]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "address2", "");
					_fieldName = "[ADDRESS3]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "address3", "");
					_fieldName = "[ADDRESS4]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "address4", "");
					//validate _businessPhysCity values
					_fieldName = "[CITY]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "city", "");
					//validate _businessPhysState values
					_fieldName = "[STATE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "state", "");
					//validate _businessPhysCountryCode values
					_fieldName = "[COUNTRY_CODE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "countryCode", "");
					//validate _businessPhysPostalCode values
					_fieldName = "[POSTAL_CODE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "postalCode", "");
					//validate _businessPhysGeoCode values
					_fieldName = "[GEO_CODE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "geoCode", "");
					//validate _businessMailAddress1 values
					queryStr1 = "SELECT t3.";
					queryStr2 = " from [BRANCH] as t1, [BUSINESS] as t2, [ADDRESS] as t3 where t1.BUSINESS_ID=t2.ID and"+
								" t2.MAILING_ADDRESS_ID=t3.ID and t1.EXTERNAL_ID = '"+_branchExternalID.get(u)+"'";
					_fieldName = "[ADDRESS1]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address1", "");
					//validate _businessMailAddress2 values
					_fieldName = "[ADDRESS2]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address2", "");
					//validate _businessMailAddress3 values
					_fieldName = "[ADDRESS3]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address3", "");
					//validate _businessMailAddress4 values
					_fieldName = "[ADDRESS4]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address4", "");
					//validate _businessMailCity values
					_fieldName = "[CITY]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "city", "");
					//validate _businessMailState values
					_fieldName = "[STATE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "state", "");
					//validate _businessMailCountryCode values
					_fieldName = "[COUNTRY_CODE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "countryCode", "");
					//validate _businessMailPostalCode values
					_fieldName = "[POSTAL_CODE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "postalCode", "");
					//validate _businessMailGeoCode values
					_fieldName = "[GEO_CODE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "geoCode", "");
					//contact1
					//firstName
					queryStr1 = "SELECT t3.";
					queryStr2 = " from [BRANCH] as t1, [BUSINESS] as t2, [PERSON] as t3"+
								" where t1.BUSINESS_ID=t2.ID and t2.CONTACT1_ID=t3.ID and t1.EXTERNAL_ID = '"+_branchExternalID.get(u)+"'";
					_fieldName = "[FIRST_NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "firstName", "");
					//middleName
					_fieldName = "[MIDDLE_NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "middleName", "");
					//lastName
					_fieldName = "[LAST_NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "lastName", "");
					//primaryEmail
					_fieldName = "[PRIMARY_EMAIL]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "primaryEmail", "");
					//alternateEmail
					_fieldName = "[ALTERNATE_EMAIL]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "alternateEmail", "");
					//homePhone
					_fieldName = "[HOME_PHONE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "homePhone", "");
					//workPhone
					_fieldName = "[WORK_PHONE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "workPhone", "");
					//mobilePhone
					_fieldName = "[MOBILE_PHONE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "mobilePhone", "");
					//employee
					_fieldName = "[EMPLOYEE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact1", "employee", "");
					//contact2
					//firstName
					queryStr1 = "SELECT t3.";
					queryStr2 = " from [BRANCH] as t1, [BUSINESS] as t2, [PERSON] as t3"+
								" where t1.BUSINESS_ID=t2.ID and t2.CONTACT2_ID=t3.ID and t1.EXTERNAL_ID = '"+_branchExternalID.get(u)+"'";
					_fieldName = "[FIRST_NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "firstName", "");
					//lastName
					_fieldName = "[LAST_NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "lastName", "");
					//primaryEmail
					_fieldName = "[PRIMARY_EMAIL]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "primaryEmail", "");
					//alternateEmail
					_fieldName = "[ALTERNATE_EMAIL]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "alternateEmail", "");
					//homePhone
					_fieldName = "[HOME_PHONE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "homePhone", "");
					//workPhone
					_fieldName = "[WORK_PHONE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "workPhone", "");
					//mobilePhone
					_fieldName = "[MOBILE_PHONE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "mobilePhone", "");
					//employee
					_fieldName = "[EMPLOYEE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "contact2", "employee", "");
				}else{
					Statement stmt = (Statement) _stmt.get(0);
				  	rs = ((Statement) stmt).executeQuery("SELECT EXTERNAL_ID from [BRANCH] where EXTERNAL_ID = '"+_branchExternalID.get(u)+"'");
				  	rs.next();
			  		try{Assert.fail(rs.getString(1) + "branch EXTERNAL_ID has been created!");}
			  		catch(SQLException e){if(e.getMessage().equals("No current row in the ResultSet.")){}}
				}

		}

		closeDBConnection(_stmt);
	}
  
  /**
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ClassNotFoundException  
   **/
  public void compareUserDataInXMLWithDB (List<Object> _stmt, String _sqlQuery, String _fileName, int _index, String _fieldName, String _parentElement, String _childElement, String _attribute) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
  {
		ResultSet rs=null;
		String xValue;
		Statement stmt = (Statement) _stmt.get(0);
	  	rs = ((Statement) stmt).executeQuery(_sqlQuery);
	  	rs.next();
	  	try{
	  		xValue= getElementValueInConduitXMLFile(_fileName, _parentElement, _childElement, _attribute).get(_index);
		  	if(xValue.equals("elementDoesNotExist"))
		  	{
		  		try{
		  			if(rs.getString(1).equals(""))
		  			{
		  				xValue="";
		  				System.out.println(_fieldName + ": - IN XML:" + xValue + "  IN DB:" +   rs.getString(1));
		  		  		Assert.assertEquals(_fieldName + " is incorrect!",xValue,  rs.getString(1));
		  				}
		  			}
		  		catch(SQLException e){if(e.getMessage().equals("No current row in the ResultSet.")){}}
		  		catch (NullPointerException e1){}}
		  	else if (xValue.toLowerCase().equals("true")){xValue = "1";}
	  		else if (xValue.toLowerCase().equals("false")){xValue = "0";}
	  		else if (xValue.equals("EmptyValue") || xValue.equals(""))
	  		{
	  		/*	try{Assert.assertEquals(_fieldName + " is incorrect!","",  rs.getString(1));}
	  			catch(ComparisonFailure e1){
	  			try{Assert.assertEquals(_fieldName + " is incorrect!",null,  rs.getString(1));}
	  			catch(ComparisonFailure e2){Assert.assertEquals(_fieldName + " is incorrect!",zero,  rs.getString(1));}}*/
  			}
		  	else{
		  		System.out.println(_fieldName + ": - IN XML:" + xValue + "  IN DB:" +   rs.getString(1));
		  		try
		  		{
			  		if(rs.getString(1).contains(" 00:00:00.0"))
			  		{
			  			Assert.assertEquals(_fieldName + " is incorrect!",xValue,  rs.getString(1).replace(" 00:00:00.0", ""));
			  		}else{
			  			try{
			  				BigDecimal validAmount = new BigDecimal(xValue);
			  				try{Assert.assertEquals(_fieldName + " is incorrect!", validAmount.toString(),  rs.getString(1));}
			  				catch(ComparisonFailure e1){Assert.assertEquals(_fieldName + " is incorrect!", validAmount.toString() + ".00",  rs.getString(1));}
			  			}
			  			catch(Exception e)
			  			{
			  				if (_fieldName.equals("[PHONE]")){
			  					Assert.assertEquals(_fieldName + " is incorrect!",xValue.trim().replace("-", ""),  rs.getString(1));
			  				}else if (_fieldName.equals("TAX_ID")){
			  					Assert.assertEquals(_fieldName + " is incorrect!",xValue.trim().replace("-", ""),  rs.getString(1));
			  				}else if (_fieldName.equals("HOME_PHONE")){
			  					Assert.assertEquals(_fieldName + " is incorrect!",xValue.trim().replace("-", ""),  rs.getString(1));
			  				}else if (_fieldName.equals("[WORK_PHONE]")){
			  					Assert.assertEquals(_fieldName + " is incorrect!",xValue.trim().replace("-", ""),  rs.getString(1));
			  				}else if (_fieldName.equals("[MOBILE_PHONE]")){
			  					Assert.assertEquals(_fieldName + " is incorrect!",xValue.trim().replace("-", ""),  rs.getString(1));
			  				}else{
			  					Assert.assertEquals(_fieldName + " is incorrect!",xValue.trim(),  rs.getString(1));
			  				}
			  			}
			  			
			  		}
		  		}
		  		catch(NullPointerException e1){Assert.fail("No data created in the DB for " +_fieldName + "!");}
		  	}	  		
	  	}
	  	catch(IndexOutOfBoundsException e){}
  }
  
  /**
   * Validate USERS, ACCOUNTS and TRANSACTIONS data
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws InterruptedException 
   */
  public void validateConduitUsersAndAccounts(String _fileName1, boolean _usersBeCreated, boolean _userAccountListBeCreated, boolean _accountListBeCreated, 
		  boolean _trUserJoinBeCreated, boolean _trBeCreated, boolean _trSplitCatBeCreated,
		  String _updateFileName, boolean _userUpdateShouldOccur, boolean _accountUpdateShouldOccur) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
	{
	  	List<Object> _stmt = connectToDB();
		ResultSet rs=null;
		String _fieldName = null, queryStr1 = null, queryStr2 = null,_fileName, xValue ="";
		Statement stmt = (Statement) _stmt.get(0);
		if(_userUpdateShouldOccur){_fileName = _updateFileName;}else{_fileName = _fileName1;}
		List<String> _userDirectID = getElementValueInConduitXMLFile(_fileName, "user", "", "directId");
		List<String> _userAccountListAccountDirectID = getElementValueInConduitXMLFile(_fileName, "userAccount", "accountDirectId", "");
		List<String> _accountListDirectID = getElementValueInConduitXMLFile(_fileName, "account", "", "directId");
		List<String> _trDirectID = getElementValueInConduitXMLFile(_fileName, "transaction", "", "directId");
		List<String> _transferToAccountDirectId = getElementValueInConduitXMLFile(_fileName, "transfer", "", "toAccountDirectId");
		List<String> _toAccountRoutingTransitNumber = getElementValueInConduitXMLFile(_fileName, "transfer", "toAccountRoutingTransitNumber", "");
		if(_usersBeCreated)
		  {	
			for (int u = 0; u <_userDirectID.size(); u++)
			{
				queryStr1 = "SELECT ";
				queryStr2 = " from [OLB_USER]  where DIRECT_ID= '"+_userDirectID.get(u)+"'";
				//validate _userDirectID values
				_fieldName = "DIRECT_ID";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "", "directId");
			  //validate _userDirectID2 values
				_fieldName = "[DIRECT_ID2]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "", "directId2");
				//validate _loginId values
				_fieldName = "LOGIN_ID";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "loginId", "");
				//validate _branchExternalId values
			  	queryStr1 = "SELECT t1.";
				queryStr2 = " from [BRANCH] as t1, [OLB_USER] as t2 where t1.ID = t2.BRANCH_ID and t2.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "EXTERNAL_ID";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "branchExternalId", "");
				//validate _interfaceId values
			  	queryStr1 = "SELECT ";
				queryStr2 = " from [OLB_USER]  where DIRECT_ID= '"+_userDirectID.get(u)+"'";
				_fieldName = "[INTERFACE_ID]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "interfaceId", "");
				//validate _taxId values
				_fieldName = "TAX_ID";
				queryStr1 = "SELECT ";
				queryStr2 = " from [OLB_USER]  where DIRECT_ID= '"+_userDirectID.get(u)+"'";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "taxId", "");
				//validate _taxIdType values
				_fieldName = "TAX_ID_TYPE";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "taxIdType", "");
				//validate _locked values
				_fieldName = "LOCKED";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "locked", "");
				//validate _userEnabled values
				_fieldName = "[USER_ENABLED]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "userEnabled", "");
				//validate _emailOptOut values
				_fieldName = "[EMAIL_OPT_OUT]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "emailOptOut", "");
				//validate _dateOfBirth values
				_fieldName = "[DATE_OF_BIRTH]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "dateOfBirth", "");
				//validate _gender values
				_fieldName = "[GENDER]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "gender", "");
				//validate _billpaySubscriberId values
				_fieldName = "[BILLPAY_SUBSCRIBER_ID]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "billpaySubscriberId", "");
				//validate _creditScore values
				_fieldName = "[CREDIT_SCORE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "creditScore", "");
				//validate _creditScoreDate values
				_fieldName = "[CREDIT_SCORE_DATE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "creditScoreDate", "");
				//validate _userClass values
				_fieldName = "[USER_CLASS]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "user", "userClass", "");
				//validate billpayStatus values
				//validate mobile values
				
				
				
			  	//PERSON	  	
				//validate _firstName values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [PERSON] as t2 where t1.PERSON_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "FIRST_NAME";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "firstName", "");
				//validate _middleName values
				_fieldName = "[MIDDLE_NAME]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "middleName", "");
			  	//validate _lastName values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [PERSON] as t2 where t1.PERSON_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "LAST_NAME";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "lastName", "");
				//validate _primaryEmail values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [PERSON] as t2 where t1.PERSON_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "PRIMARY_EMAIL";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "primaryEmail", "");
			    //validate _alternateEmail values
				_fieldName = "[ALTERNATE_EMAIL]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "alternateEmail", "");
				//validate _homePhone values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [PERSON] as t2 where t1.PERSON_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "HOME_PHONE";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "homePhone", "");
			    //validate _workPhone values
				_fieldName = "[WORK_PHONE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "workPhone", "");
			    //validate _mobilePhone values
				_fieldName = "[MOBILE_PHONE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "mobilePhone", "");		  	
			  	//validate _employee values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [PERSON] as t2 where t1.PERSON_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "EMPLOYEE";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "person", "employee", "");
			  	//physicalAddress
				//validate _address1 values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [ADDRESS] as t2, [PERSON] as t3 where t1.PERSON_ID=t3.ID and" + 
						"  t3.PHYSICAL_ADDRESS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "ADDRESS1";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "address1", "");
			    //validate _address2 values
				_fieldName = "[ADDRESS2]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "_address2", "");
			    //validate _address3 values
				_fieldName = "[ADDRESS3]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "_address3", "");
			    //validate _address4 values
				_fieldName = "[ADDRESS4]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "_address4", "");	
				//validate city values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [ADDRESS] as t2, [PERSON] as t3 where t1.PERSON_ID=t3.ID and" + 
						"  t3.PHYSICAL_ADDRESS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "[CITY]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "city", "");	
				//validate _state values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [ADDRESS] as t2, [PERSON] as t3 where t1.PERSON_ID=t3.ID and" + 
						"  t3.PHYSICAL_ADDRESS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "[STATE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "state", "");
				//validate _countryCode values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [ADDRESS] as t2, [PERSON] as t3 where t1.PERSON_ID=t3.ID and" + 
						"  t3.PHYSICAL_ADDRESS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "[COUNTRY_CODE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "countryCode", "");
				//validate _postalCode values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [ADDRESS] as t2, [PERSON] as t3 where t1.PERSON_ID=t3.ID and" + 
						"  t3.PHYSICAL_ADDRESS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "[POSTAL_CODE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "postalCode", "");
			    //validate _geoCode values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [ADDRESS] as t2, [PERSON] as t3 where t1.PERSON_ID=t3.ID and" + 
						"  t3.PHYSICAL_ADDRESS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "[GEO_CODE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "physicalAddress", "geoCode", "");
			  	//mailingAddress
			    //validate _address1 values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [ADDRESS] as t2, [PERSON] as t3 where t1.PERSON_ID=t3.ID and" + 
						"  t3.MAILING_ADDRESS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "[ADDRESS1]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address1", "");
			  //validate _address2 values
				_fieldName = "[ADDRESS2]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address2", "");
			  //validate _address3 values
				_fieldName = "[ADDRESS3]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address3", "");
			  //validate _address4 values
				_fieldName = "[ADDRESS4]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "address4", "");
			  //validate _city values
				_fieldName = "[CITY]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "city", "");
			  //validate _state values
				_fieldName = "[STATE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "state", "");
			  //validate _countryCode values
				_fieldName = "[COUNTRY_CODE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "countryCode", "");
			  //validate _postalCode values
				_fieldName = "[POSTAL_CODE]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "mailingAddress", "postalCode", "");
			  	//credentials
				//validate _profileName values
			  	queryStr1 = "SELECT t2.";
				queryStr2 = " from [OLB_USER] as t1, [CREDENTIALS] as t2 where t1.CREDENTIALS_ID=t2.ID and t1.DIRECT_ID = '"+_userDirectID.get(u)+"'";
				_fieldName = "[PROFILE_NAME]";
				compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "credentials", "profileName", "");
				//validate _secretQuestion1 values
			  	_fieldName = "[SECRET_QUESTION1]";
			  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "credentials", "secretQuestion1", "");
				//validate _secretQuestion2 values
			  	_fieldName = "[SECRET_QUESTION2]";
			  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "credentials", "secretQuestion2", "");
				//validate _secretQuestion3 values
			  	_fieldName = "[SECRET_QUESTION3]";
			  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  u,  _fieldName,  "credentials", "secretQuestion3", "");
			}	
		  }else{
			  	for (int u = 0; u<_userDirectID.size(); u++)
			  	{
				  	rs = ((Statement) stmt).executeQuery("SELECT COUNT(DIRECT_ID) FROM [OLB_USER] where DIRECT_ID = '"+_userDirectID.get(u)+"'");
				  	rs.next();
				  	try 
				  	{
				  		Assert.assertEquals("Expected no user data to be created, but it did!","0",  rs.getString(1));
				  	}
				  	catch (SQLException e)
				  	{	
				  	}
			  	}
		  }
		
		if(_userAccountListBeCreated)
		  {
			for (int a=0; a<_userAccountListAccountDirectID.size(); a++)
			{
				String _accountType = "internal";
				String accUniqueID = "DIRECT_ID";
				if(_userAccountListAccountDirectID.get(a).equals("elementDoesNotExist"))
				{
					_userAccountListAccountDirectID.set(a, getElementValueInConduitXMLFile(_fileName, "userAccount", "accountInterfaceId", "").get(a));
					_accountType = "external";
				}
								
				try
				{
					if (_accountType.equals("external"))
					{
						accUniqueID = "[INTERFACE_ID]";
					}else if (_accountType.equals("internal")){
						accUniqueID = "[DIRECT_ID]";
					}else{
						accUniqueID = "[NICKNAME]";
					}
					//validate _accountDirectId values
					_fieldName = accUniqueID;
					queryStr1 = "SELECT t1.";
					queryStr2 = " FROM [OLB_ACCOUNT] as t1, [USER_ACCOUNT_JOIN] as t2 where t1.ACCOUNT_ID = t2.ACCOUNT_ID and t1."+accUniqueID+" = '"+_userAccountListAccountDirectID.get(a)+"'";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "userAccount", "accountInterfaceId", "");
					//validate _nickname values
				  	queryStr1 = "SELECT t2.";
					_fieldName = "[NICKNAME]";
					String _nickName = getElementValueInConduitXMLFile(_fileName, "userAccount", "nickname", "").get(a);
					if (!_nickName.equals("elementDoesNotExist"))
					{
						queryStr2 = " FROM [OLB_ACCOUNT] as t1, [USER_ACCOUNT_JOIN] as t2 where t1.ACCOUNT_ID = t2.ACCOUNT_ID and t1."+accUniqueID+" = '"+_userAccountListAccountDirectID.get(a)+"'"
								+ "and t2.NICKNAME = '"+_nickName+"'" ;
					}
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "userAccount", "nickname", ""); 
					//validate _excluded values
					queryStr2 = " FROM [OLB_ACCOUNT] as t1, [USER_ACCOUNT_JOIN] as t2 where t1.ACCOUNT_ID = t2.ACCOUNT_ID and t1."+accUniqueID+" = '"+_userAccountListAccountDirectID.get(a)+"'";
					_fieldName = "[EXCLUDED]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "userAccount", "excluded", "");
					//validate _hidden values
					_fieldName = "[HIDDEN]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "userAccount", "hidden", "");
					//validate _hideByAdmin values
					_fieldName = "[HIDE_BY_ADMIN]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "userAccount", "hideByAdmin", "");
					//validate _displayOrder values
					_fieldName = "[DISPLAY_ORDER]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "userAccount", "displayOrder", ""); 
				}
			  	catch (SQLException e)
			  	{
			  		Assert.fail("No userAccountList record has been created!");	
			  	}
			}
		  }else{
			  	for (int u = 0; u<_userAccountListAccountDirectID.size(); u++)
			  	{
				  	rs = ((Statement) stmt).executeQuery("SELECT COUNT(t2.ACCOUNT_ID) FROM [OLB_ACCOUNT] as t1, [USER_ACCOUNT_JOIN] as t2 where t1.ACCOUNT_ID = t2.ACCOUNT_ID " +
				  			"and t1.DIRECT_ID = '"+_userAccountListAccountDirectID.get(u)+"'");
				  	rs.next();
				  	try 
				  	{
				  		Assert.assertEquals("Expected no user accountList data to be created, but it did!","0",  rs.getString(1));
				  	}
				  	catch (SQLException e)
				  	{
				  		Assert.assertEquals("Expected no userAccountList data to be created, but it did!","No current row in the ResultSet.",  e.getMessage());	
				  	}
			  	}
		  }
		if(_accountUpdateShouldOccur){_fileName = _updateFileName;}else{_fileName = _fileName1;}
		_accountListDirectID = getElementValueInConduitXMLFile(_fileName, "account", "", "directId");
		_trDirectID = getElementValueInConduitXMLFile(_fileName, "transaction", "", "directId");
		String _accountType = "internal";
		String _trType = "internal";
		if(_accountListBeCreated)
		  {
			for (int a=0; a<_accountListDirectID.size(); a++)
			{
				_accountType = "internal";
				_trType = "internal"; 
				
				if(_accountListDirectID.get(a).equals("elementDoesNotExist"))
				{
					_accountListDirectID.set(a, getElementValueInConduitXMLFile(_fileName, "account", "", "interfaceId").get(a));
					_accountType = "external";
					if(_accountListDirectID.get(a).equals("elementDoesNotExist"))
					{
						_accountListDirectID.set(a, getElementValueInConduitXMLFile(_fileName, "account", "accountName", "").get(a));
						_accountType = "offline";
					}
				}				
				try
				{
					queryStr1 = "SELECT ";
					if (_accountType.equals("external"))
					{
						queryStr2 = " from [OLB_ACCOUNT] where [INTERFACE_ID] = '"+_accountListDirectID.get(a)+"'";
					}else if (_accountType.equals("internal")){
						queryStr2 = " from [OLB_ACCOUNT] where DIRECT_ID = '"+_accountListDirectID.get(a)+"'";
					}else{
						queryStr2 = " from [OLB_ACCOUNT] where [ACCOUNT_NAME] = '"+_accountListDirectID.get(a)+"'";
					}
					//validate _accountDirectId values
					_fieldName = "[DIRECT_ID]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountDirectId", "");
				    //interfaceId
					_fieldName = "[INTERFACE_ID]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "interfaceId", "");
				    //accountName
					_fieldName = "[ACCOUNT_NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountName", "");
				  	//accountNumber
					_fieldName = "[ACCOUNT_NUMBER]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountNumber", "");
				  	//accountClass
					_fieldName = "[ACCOUNT_CLASS]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountClass", "");
				  	//accountType
					_fieldName = "[ACCOUNT_TYPE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountType", "");
				  	//balance
					_fieldName = "[BALANCE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "balance", "");
/*				  	//branchExternalId
 *                  _fieldName = "[BRANCH_ID]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "branchExternalId", "");
					//accountProductExternalId
				  	queryStr1 = "SELECT ";
					queryStr2 = " from [OLB_ACCOUNT] where DIRECT_ID = '"+_accountListDirectID.get(a)+"'";
				  	_fieldName = "[ACCOUNT_PRODUCT_ID]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountProductExternalId", "");   //OLB632
*/					//accountStatus
				  	_fieldName = "[ACCOUNT_STATUS]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountStatus", "");
					//availableBalance
				  	_fieldName = "[AVAILABLE_BALANCE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "availableBalance", "");
					//currencyCode
				  	_fieldName = "[CURRENCY_CODE]";
				  	try {compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "currencyCode", "");}catch (AssertionFailedError e1){}
					//rateOfReturn
				  	_fieldName = "[RATE_OF_RETURN]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "rateOfReturn", "");
					//availableCredit
				  	_fieldName = "[AVAILABLE_CREDIT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "availableCredit", "");
					//restricted
				  	_fieldName = "[RESTRICTED]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "restricted", "");
					//estatements
				  	_fieldName = "[ESTATEMENTS]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "estatements", "");
					//accountOpenDate
				  	_fieldName = "[ACCOUNT_OPEN_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountOpenDate", "");
					//lastStatementDate
				  	_fieldName = "[LAST_STATEMENT_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "lastStatementDate", "");
					//lastDepositAmount
				  	_fieldName = "[LAST_DEPOSIT_AMOUNT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "lastDepositAmount", "");
					//lastDepositDate
				  	_fieldName = "[LAST_DEPOSIT_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "lastDepositDate", "");
					//rewardsBalance
				  	_fieldName = "[REWARDS_BALANCE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "rewardsBalance", "");
					//pointsAccrued
				  	_fieldName = "[POINTS_ACCRUED]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "pointsAccrued", "");
					//pointsRedeemed
				  	_fieldName = "[POINTS_REDEEMED]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "pointsRedeemed", "");
				    //externalInstitutionId
				  	_fieldName = "[INSTITUTION_ID]";
				  	//compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "externalInstitutionId", "");
				  	//accountStatus
				  	_fieldName = "[ACCOUNT_STATUS]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountStatus", "");
				  	//creditLimit
				  	_fieldName = "[CREDIT_LIMIT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "creditLimit", "");
				  	//availableCredit
				  	_fieldName = "[AVAILABLE_CREDIT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "availableCredit", "");
				  	//cashAdvanceLimit
				  	_fieldName = "[CASH_ADVANCE_LIMIT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "cashAdvanceLimit", "");
				  	//availableCashLimit
				  	_fieldName = "[AVAILABLE_CASH_LIMIT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "availableCashLimit", "");
				  	//minimumPaymentDue
				  	_fieldName = "[MINIMUM_PAYMENT_DUE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "minimumPaymentDue", "");
				  	//paymentDueDate
				  	_fieldName = "[PAYMENT_DUE_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "paymentDueDate", "");
				  	//interestRate
				  	_fieldName = "[INTEREST_RATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "interestRate", "");
				  	//originationDate
				  	_fieldName = "[ORIGINATION_DATE]";
				  	try {compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "originationDate", "");}catch (AssertionFailedError e1){}
				  	//maturityDate
				  	_fieldName = "[MATURITY_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "maturityDate", "");
				  	//previousAmountDue
				  	_fieldName = "[PREVIOUS_AMOUNT_DUE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "previousAmountDue", "");
				  	//pastAmountDue
				  	_fieldName = "[PAST_AMOUNT_DUE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "pastAmountDue", "");
				  	//financeCharges
				  	_fieldName = "[FINANCE_CHARGES]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "financeCharges", "");
				  	//holdAmount
				  	_fieldName = "[HOLD_AMOUNT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "holdAmount", "");
				  	//accountClosedDate
				  	_fieldName = "[ACCOUNT_CLOSED_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "accountClosedDate", "");
				  	//aggregationStatus
				  	_fieldName = "[AGGREGATION_STATUS]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "aggregationStatus", "");
				  	//lastPaymentDate
				  	_fieldName = "[LAST_PAYMENT_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "lastPaymentDate", "");
				  	//lastPaymentAmount
				  	_fieldName = "[LAST_PAYMENT_AMOUNT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "lastPaymentAmount", "");
				  	//loanDate
				  	_fieldName = "[LOAN_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "loanDate", "");
				  	//apr
				  	_fieldName = "[APR]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "apr", "");
				  	//ROUTING_TRANSIT_NUMBER
				  	_fieldName = "[ROUTING_TRANSIT_NUMBER]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "account", "routingTransitNumber", "");
				}
			  	catch (SQLException e)
			  	{
			  		if(!xValue.equals("elementDoesNotExist"))
			  		{
			  			Assert.fail(_fieldName + " values has been NOT created in the DB or not same as in the XML file.");
			  		}	
			  	}
			}
		  }else{
			  	for (int u = 0; u<_accountListDirectID.size(); u++)
			  	{

			  		if(!_accountListDirectID.get(u).trim().equals("") || !_accountListDirectID.get(u).equals("elementDoesNotExist"))
			  		{
					  	rs = ((Statement) stmt).executeQuery("SELECT COUNT(*) from [OLB_ACCOUNT] where DIRECT_ID = '"+_accountListDirectID.get(u)+"'");
					  	rs.next();
					  	try 
					  	{
					  		Assert.assertEquals("Expected no accountList data to be created, but it did!","0",  rs.getString(1));
					  	}
					  	catch (SQLException e)
					  	{
					  		Assert.assertEquals("Expected no accountList data to be created, but it did!","No current row in the ResultSet.",  e.getMessage());	
					  	}
			  		}
			  	}
		}
		
		if(_trBeCreated)
		  {
			for (int a=0; a<_trDirectID.size(); a++)
			{

				if(_trDirectID.get(a).equals("elementDoesNotExist"))
				{
					_trDirectID.set(a, getElementValueInConduitXMLFile(_fileName, "transaction", "", "interfaceId").get(a));
					_trType = "external";
					if(_trDirectID.get(a).equals("elementDoesNotExist"))
					{
						_trDirectID.set(a, getElementValueInConduitXMLFile(_fileName, "transaction", "name", "").get(a));
						_trType = "offline";
					}
				}
				
				
				try
				{
					queryStr1 = "SELECT ";
					if (_trType.equals("external"))
					{
						queryStr2 = " from [OLB_TRANSACTION] where [INTERFACE_ID] = '"+_trDirectID.get(a)+"'";
					}else if (_trType.equals("internal")){
						queryStr2 = " from [OLB_TRANSACTION] where DIRECT_ID = '"+_trDirectID.get(a)+"'";
					}else{
						queryStr2 = " from [OLB_TRANSACTION] where [NAME] = '"+_trDirectID.get(a)+"'";
					}
					//validate _trDirectID values
					_fieldName = "DIRECT_ID";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "", "directId");
					_fieldName = "[INTERFACE_ID]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "", "interfaceId");
					//validate _amount values
					_fieldName = "[AMOUNT]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "amount", "");
					//validate _name values
					_fieldName = "[NAME]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "name", "");
					//validate _postDate values
					_fieldName = "[POST_DATE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "postDate", "");
					//validate _type values
					_fieldName = "[TYPE]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "type", "");
					//validate _pending values
					_fieldName = "[PENDING]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "pending", "");
					//validate _memo values
					_fieldName = "[MEMO]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "memo", "");
					//validate _mcc values
					_fieldName = "[MCC]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "mcc", "");
                    //principalAmount
					_fieldName = "[PRINCIPAL_AMOUNT]";
					compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "principalAmount", "");
					//interestAmount
				  	_fieldName = "[INTEREST_AMOUNT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "interestAmount", "");
					//otherAmount
				  	_fieldName = "[OTHER_AMOUNT]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "otherAmount", "");
					//currencyCode
				  	_fieldName = "[CURRENCY_CODE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "currencyCode", "");
					//runningBalance
				  	_fieldName = "[RUNNING_BALANCE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "runningBalance", "");
					//originationDate
				  	_fieldName = "[ORIGINATION_DATE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "originationDate", "");
					//tranCode
				  	_fieldName = "[TRAN_CODE]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "tranCode", "");
					//checknum
				  	_fieldName = "[CHECKNUM]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "checknum", "");
					//imageId
				  	_fieldName = "[IMAGE_ID]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "imageId", "");
					//postingSeq
				  	_fieldName = "[POSTING_SEQ]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "postingSeq", "");		
				   //DEPOSIT_NUM
				  	_fieldName = "[DEPOSIT_NUM]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "depositNum", "");
				   //CONFIRMATION_NUM
				  	_fieldName = "[CONFIRMATION_NUM]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "confirmationNum", "");
				   //STATUS
				  	_fieldName = "[STATUS]";
				  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  a,  _fieldName,  "transaction", "status", "");			  	
				  	for (int z=0; z<_transferToAccountDirectId.size(); z++)
					{
				  		if(!_transferToAccountDirectId.get(z).equals("elementDoesNotExist"))
				  		{
					  	    //toAccountDirectId
					  		queryStr2 = " from [TRANSFER] where [TO_ACCOUNT_DIRECT_ID] = '"+_transferToAccountDirectId.get(z)+"' and [TO_ACCOUNT_ROUTING_TRANSIT_NUMBER] = '"
					  				+ _toAccountRoutingTransitNumber.get(z) + "'";
						  	_fieldName = "[TO_ACCOUNT_DIRECT_ID]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName, z,  _fieldName,  "transfer", "", "toAccountDirectId");
						  	//toAccountName
						  	_fieldName = "[TO_ACCOUNT_NAME]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  z,  _fieldName,  "transfer", "toAccountName", "");
						  	//toAccountRoutingTransitNumber
						  	_fieldName = "[TO_ACCOUNT_ROUTING_TRANSIT_NUMBER]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  z,  _fieldName,  "transfer", "toAccountRoutingTransitNumber", "");
						  	//toAccountNumber
						  	_fieldName = "[TO_ACCOUNT_NUMBER]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  z,  _fieldName,  "transfer", "toAccountNumber", "");
						  	//toAccountType
						  	_fieldName = "[TO_ACCOUNT_TYPE]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  z,  _fieldName,  "transfer", "toAccountType", "");
						  	//frequency
						  	_fieldName = "[FREQUENCY]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  z,  _fieldName,  "transfer", "frequency", "");
						  	//startDate
						  	_fieldName = "[START_DATE]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  z,  _fieldName,  "transfer", "startDate", "");
						  	//endDate
						  	_fieldName = "[END_DATE]";
						  	compareUserDataInXMLWithDB(_stmt, queryStr1+_fieldName+queryStr2,  _fileName,  z,  _fieldName,  "transfer", "endDate", "");
				  		}
					}	
				}
			  	catch (SQLException e)
			  	{
			  		if(!xValue.equals("elementDoesNotExist"))
			  		{
			  			Assert.fail("No transaction record has been created!");	
			  		}
			  	}
			}
		  }else{
			  	for (int u = 0; u<_trDirectID.size(); u++)
			  	{
			  		if (!_trDirectID.get(u).trim().equals(""))
			  		{
					  	rs = ((Statement) stmt).executeQuery("SELECT COUNT(*) from [OLB_TRANSACTION] where DIRECT_ID = '"+_trDirectID.get(u)+"'");
					  	rs.next();
					  	try 
					  	{
					  		Assert.assertEquals("Expected no accountList data to be created, but it did!","0",  rs.getString(1));
					  	}
					  	catch (SQLException e)
					  	{
					  		Assert.assertEquals("Expected no OLB_TRANSACTION data to be created, but it did!","No current row in the ResultSet.",  e.getMessage());	
					  	}	
			  		}
			  	}
		  }	
		if(_trUserJoinBeCreated)
		  {
			_trDirectID = getElementValueInConduitXMLFile(_fileName, "transaction", "", "directId");
			_trType = "internal";
		  	for (int u = 0; u<_trDirectID.size(); u++)
		  	{	  		
		  		
				if(_trDirectID.get(u).equals("elementDoesNotExist"))
				{
					_trDirectID.set(u, getElementValueInConduitXMLFile(_fileName, "transaction", "", "interfaceId").get(u));
					_trType = "external";
					if(_trDirectID.get(u).equals("elementDoesNotExist"))
					{
						_trDirectID.set(u, getElementValueInConduitXMLFile(_fileName, "transaction", "name", "").get(u));
						_trType = "offline";
					}
				}
				
				if (_trType.equals("external"))
				{
					queryStr1 = "select t2.[INTERFACE_ID] from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2 where t1.TRANSACTION_ID=t2.ID and t2.[INTERFACE_ID]='"+_trDirectID.get(u)+"'";
				}else if (_trType.equals("internal")){
					queryStr1 = "select t2.DIRECT_ID from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2 where t1.TRANSACTION_ID=t2.ID and t2.DIRECT_ID='"+_trDirectID.get(u)+"'";
				}else{
					queryStr1 = "select t2.NAME from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2 where t1.TRANSACTION_ID=t2.ID and t2.[NAME]='"+_trDirectID.get(u)+"'";
				}
			  	rs = ((Statement) stmt).executeQuery(queryStr1);
			  	rs.next();
			  	try 
			  	{
			  		Assert.assertEquals("USER_TRANSACTION_JOIN record has not been created or incorrect!",_trDirectID.get(u),rs.getString(1));
			  	}
			  	catch (SQLException e)
			  	{
			  		if (!_trDirectID.get(u).equals("elementDoesNotExist"))
			  		{
			  			Assert.fail("No [USER_TRANSACTION_JOIN] data to be created, but it did!");	
			  		}
			  	}
		  	}
		  }else{
			  	for (int u = 0; u<_trDirectID.size(); u++)
			  	{
					if (_trType.equals("external"))
					{
						queryStr1 = "select COUNT(t1.ID) from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2 where t1.TRANSACTION_ID=t2.ID and t2.[INTERFACE_ID]='"+_trDirectID.get(u)+"'";
					}else if (_trType.equals("internal")){
						queryStr1 = "select COUNT(t1.ID) from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2 where t1.TRANSACTION_ID=t2.ID and t2.DIRECT_ID='"+_trDirectID.get(u)+"'";
					}else{
						queryStr1 = "select COUNT(t1.ID) from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2 where t1.TRANSACTION_ID=t2.ID and t2.[NAME]='"+_trDirectID.get(u)+"'";
					}
			  		rs = ((Statement) stmt).executeQuery(queryStr1);
			  		rs.next();
				  	try 
				  	{
				  		Assert.assertEquals("Expected no accountList data to be created, but it did!","0",  rs.getString(1));
				  	}
				  	catch (SQLException e)
				  	{
				  		Assert.assertEquals("Expected no [USER_TRANSACTION_JOIN] data to be created, but it did!","No current row in the ResultSet.",  e.getMessage());	
				  	}
			  	}
		  }	
		if(_trSplitCatBeCreated)
		  {
			_trDirectID = getElementValueInConduitXMLFile(_fileName, "transaction", "", "directId");
			_trType = "internal";	
		  	for (int u = 0; u<_trDirectID.size(); u++)
		  	{
				if(_trDirectID.get(u).equals("elementDoesNotExist"))
				{
					_trDirectID.set(u, getElementValueInConduitXMLFile(_fileName, "transaction", "", "interfaceId").get(u));
					_trType = "external";
					if(_trDirectID.get(u).equals("elementDoesNotExist"))
					{
						_trDirectID.set(u, getElementValueInConduitXMLFile(_fileName, "transaction", "name", "").get(u));
						_trType = "offline";
					}
				}
				
				if (_trType.equals("external"))
				{
					queryStr1 = "select t2.INTERFACE_ID from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2," + 
				  			"  [SPLIT_CATEGORIZATION] as t3 where t1.TRANSACTION_ID=t2.ID and t1.ID=t3.USER_TRANSACTION_JOIN_ID and t2.[INTERFACE_ID]='"+_trDirectID.get(u)+"'";
				}else if (_trType.equals("internal")){
					queryStr1 = "select t2.DIRECT_ID from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2," + 
				  			"  [SPLIT_CATEGORIZATION] as t3 where t1.TRANSACTION_ID=t2.ID and t1.ID=t3.USER_TRANSACTION_JOIN_ID and t2.DIRECT_ID='"+_trDirectID.get(u)+"'";
				}else{
					queryStr1 = "select t2.NAME from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2," + 
				  			"  [SPLIT_CATEGORIZATION] as t3 where t1.TRANSACTION_ID=t2.ID and t1.ID=t3.USER_TRANSACTION_JOIN_ID and t2.[NAME]='"+_trDirectID.get(u)+"'";
				}		
			  	rs = ((Statement) stmt).executeQuery(queryStr1);
			  	rs.next();
			  	try 
			  	{
			  		Assert.assertEquals("SPLIT_CATEGORIZATION record has not been created or incorrect!",_trDirectID.get(u),rs.getString(1));
			  	}
			  	catch (SQLException e)
			  	{
			  		if (!_trDirectID.get(u).equals("elementDoesNotExist"))
			  		{
			  			Assert.fail("No [SPLIT_CATEGORIZATION] data to be created, but it did!");	
			  		}	
			  	}
		  	}
		  }else{
			  	for (int u = 0; u<_trDirectID.size(); u++)
			  	{
					if (_trType.equals("external"))
					{
						queryStr1 = "select COUNT(t1.ID) from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2," +
					  			"  [SPLIT_CATEGORIZATION] as t3 where t1.TRANSACTION_ID=t2.ID and t1.ID=t3.USER_TRANSACTION_JOIN_ID and t2.[INTERFACE_ID]='"+_trDirectID.get(u)+"'";
					}else if (_trType.equals("internal")){
						queryStr1 = "select COUNT(t1.ID) from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2," +
					  			"  [SPLIT_CATEGORIZATION] as t3 where t1.TRANSACTION_ID=t2.ID and t1.ID=t3.USER_TRANSACTION_JOIN_ID and t2.DIRECT_ID='"+_trDirectID.get(u)+"'";
					}else{
						queryStr1 = "select COUNT(t1.ID) from [USER_TRANSACTION_JOIN] as t1, [OLB_TRANSACTION] as t2," + 
					  			"  [SPLIT_CATEGORIZATION] as t3 where t1.TRANSACTION_ID=t2.ID and t1.ID=t3.USER_TRANSACTION_JOIN_ID and t2.[NAME]='"+_trDirectID.get(u)+"'";
					}
					
			  		rs = ((Statement) stmt).executeQuery(queryStr1);
				  	rs.next();
				  	try 
				  	{
				  		Assert.assertEquals("Expected no accountList data to be created, but it did!","0",  rs.getString(1));
				  	}
				  	catch (SQLException e)
				  	{
				  		Assert.assertEquals("Expected no [SPLIT_CATEGORIZATION] data to be created, but it did!","No current row in the ResultSet.",  e.getMessage());	
				  	}
			  	}
		  }	
		closeDBConnection(_stmt);
	}
  
  /**
   * Format string to currency
   */
  public List<String> formatToCurrency(List<String> _list)
  {
		for(int a=0; a < _list.size(); a++)
		{	        
	         DecimalFormat format = new DecimalFormat("0.00");
	         if(!_list.get(a).equals(""))
	        		 {
				         format.format(Double.valueOf(_list.get(a)));
				         String formattedValue = format.format(Double.valueOf(_list.get(a)));
				         _list.remove(a);
				         _list.add(a, formattedValue);
	        		 }
	         else
			         {
				         _list.remove(a);
				         _list.add(a, ""); 
			         }
		} 
		return _list;
  }
  
  /**
   * Conduit folder path where files get processed
   */
  public String folderPathWhereDropFile() throws ParserConfigurationException, SAXException, IOException
  {
	  	String pathToDropFile = null;
	  	pathToDropFile = readXMLData.getTestDataFor("conduitDirPath").get(0);
		return pathToDropFile;	
  }
  
  
  /**
   * run query, return string or list
 * @throws InterruptedException 
   */
  public String runQuery(List<Object> _stmt, String _sqlQuery, boolean _returnNoResult) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException, InterruptedException
	{
		ResultSet rs=null;
		String output = null;
		Statement stmt = (Statement) _stmt.get(0);
		if (_returnNoResult)
		{
			try{stmt.execute(_sqlQuery);}catch(NullPointerException e)
			{
				Thread.sleep(3000); 
				try{stmt.execute(_sqlQuery);}catch(NullPointerException ex){}
			}
		}
		else
		{
			try{rs = stmt.executeQuery(_sqlQuery);}catch(NullPointerException e)
			{
				Thread.sleep(3000); 
				try{rs = stmt.executeQuery(_sqlQuery);}catch(NullPointerException ex){}
			}
			while (rs.next())
			{
				output = (rs.getString(1));
			}				
		}
		closeDBConnection(_stmt);
		return output;
	}
  
  public String runQuery(List<Object> _stmt, String _sqlQuery, boolean _returnNoResult, boolean _noConnectionClose) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException, InterruptedException
	{
		ResultSet rs=null;
		String output = null;
		Statement stmt = (Statement) _stmt.get(0);
		if (_returnNoResult)
		{
			try{stmt.execute(_sqlQuery);}catch(NullPointerException e){}
		}
		else
		{
			rs = stmt.executeQuery(_sqlQuery);
			while (rs.next())
			{
				output = (rs.getString(1));
			}				
		}
		return output;
	}
  

  public List<String> runQuery2(List<Object> _stmt, String _sqlQuery, boolean _returnNoResult) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException, InterruptedException
	{
		ResultSet rs=null;
		List<String>  output = new ArrayList<String>();
		Statement stmt = (Statement) _stmt.get(0);
		if (_returnNoResult)
		{
			stmt.execute(_sqlQuery);
		}
		else
		{
			rs = stmt.executeQuery(_sqlQuery);
			while (rs.next())
			{
				output.add(rs.getString(1));
			}				
		}
		closeDBConnection(_stmt);
		return output;
	}
  
  /**
   * VERIFY CATEGORYID IN THE [SPLIT_CATEGORIZATION] TABLE
 * @return 
 * @throws InterruptedException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public void verifyCategoryID (String _userID, int _categoryID, String _fileName, boolean _isMultVerification, boolean _isSharedAccountTest) throws InterruptedException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException{
	  	Thread.sleep(3000);
		if (_isMultVerification)
		{
			List<String> _transactions = getElementValueInConduitXMLFile(_fileName, "transaction", "name", "");
			String _totalSplitCategorizationRecord = runQuery(connectToDB(),"SELECT COUNT(*) from [SPLIT_CATEGORIZATION]", false);
			if (_isSharedAccountTest)
			{
				Assert.assertEquals("Not all transactions have been categorized.", _transactions.size(), Integer.parseInt(_totalSplitCategorizationRecord)/2);
			}
			else
			{
				Assert.assertEquals("Not all transactions have been categorized.", _transactions.size(), Integer.parseInt(_totalSplitCategorizationRecord));
			}
			List<String> _incorrectCategorizedTrans = new ArrayList<String>();
			_incorrectCategorizedTrans.addAll(runQuery2(connectToDB(), "SELECT t1.NAME" + 
					"  FROM [OLB_TRANSACTION] as t1, [USER_TRANSACTION_JOIN] as t2, [SPLIT_CATEGORIZATION] as t3" + 
					"  where t1.ID = t2.TRANSACTION_ID and t2.ID = t3.USER_TRANSACTION_JOIN_ID and t3.CATEGORY_ID = 100 or t3.CATEGORY_ID = 1", false));
			if (_incorrectCategorizedTrans.size() > 0)
			{
				String _trans = "";
				for (int i = 0; i< _incorrectCategorizedTrans.size(); i++)
				{
					if(_trans.equals(""))
					{
						_trans = _incorrectCategorizedTrans.get(i);
					}
					else
					{
						_trans =_trans + "; "+  _incorrectCategorizedTrans.get(i);
					}
				}
				Assert.fail(_trans.toUpperCase() + " transactions are not categorized as accordingly!");
			}
		}
		else
		{
		    String _query = "select ID from [USER_TRANSACTION_JOIN] where USER_ID ='"+_userID+"'";
			String _USER_TRANSACTION_JOIN_ID = runQuery(connectToDB(),_query, false);
			_query = "select category_id from [SPLIT_CATEGORIZATION] where USER_TRANSACTION_JOIN_ID = '"+_USER_TRANSACTION_JOIN_ID+"'";
			String CATEGORY_ID = runQuery(connectToDB(),_query, false);
			Assert.assertEquals("Transaction did not get categorized correctly (USER_TRANSACTION_JOIN_ID = "+ _USER_TRANSACTION_JOIN_ID + "). CategoryID:", _categoryID, Integer.parseInt((CATEGORY_ID)));
		}
	
  }
  
  /**
   * DELETE THE USER-DEFINED CATEGORIZATION RULE
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws InterruptedException 
   */
  public void deleteCategorizationRule (String _userID, int _categoryID) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException{
		String _query = "delete [CATEGORIZATION_RULE] where USER_ID = '"+_userID+"' AND CATEGORY_ID = '"+_categoryID+"'";
		List<Object> _stmt = connectToDB();
		runQuery(_stmt, _query, true);
  }
  
  /**
   * START TOMCAT SERVER
 * @throws IOException 
 * @throws InterruptedException 
   */
  public void startTomcatServer () throws IOException, InterruptedException {
	ProcessBuilder process = new ProcessBuilder("cmd", "/C", "start", _tomcatPath+"startup.bat");
	File userDir = new File(_tomcatPath);
	process = process.directory(userDir);
	process.start();
	Thread.sleep(60000);
  }
  
  /**
   * STOP TOMCAT SERVER
 * @throws IOException 
 * @throws InterruptedException 
   */
  public void stopTomcatServer () throws IOException, InterruptedException {
	Thread.sleep(1000);
	ProcessBuilder process = new ProcessBuilder("cmd", "/C", "start", _tomcatPath+"shutdown.bat");
	File userDir = new File(_tomcatPath);
	process = process.directory(userDir);
	process.start();
	Thread.sleep(2000);
	Runtime.getRuntime().exec("TASKKILL /F /IM cmd.exe /T");
	Thread.sleep(2000);
  }
  
  
  /**
   * CREATE CATEGORIZATION TEST DATA
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws InterruptedException 
   */
  public String createUserForCategorizationTest (String _fileName) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException, InterruptedException {
		//CREATE A TEST USER
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		//GET USERID
		String _userID = "";
		List<String> _userDirectIDs   = new ArrayList<String>();
		int _loopExit = 0;
		while (_userID.equals(""))
		{
			_userDirectIDs = getElementValueInConduitXMLFile(_fileName, "user", "", "directId"); 
			String _queryToGetUserID = "select USER_ID from [OLB_USER] where DIRECT_ID = '"+_userDirectIDs.get(0)+"'";
			List<Object> _stmt = connectToDB();
			Thread.sleep(5000);
			_userID=runQuery(_stmt, _queryToGetUserID, false);
			if (_loopExit > 60)
			{
				Assert.fail("Conduit did not create the user! Please verify conduit processing!");
				break;
			}
		}
		return _userID;
  }
  
  /**
   * CLEAN-UP CATEGORIZATION TEST DATA 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws InterruptedException 
   */
  public void deleteAllTestData() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException, InterruptedException 
  {
	  try
	  {
			runQuery(connectToDB(), 
					"delete from [OLB_USER_MESSAGE]\r\n" + 
					"delete from [ALERT_JOURNAL_PROPS]\r\n" + 
					"delete from [ALERT_JOURNAL]\r\n" + 
					"delete from [ALERT_USER_DEST_JOIN] where id>4\r\n" + 
					"delete from [ALERT_USER_DEST] where USER_DEST_ID > 4 \r\n" + 
					"delete from [ALERT_DEST_TYPE] where DEST_TYPE_ID > 5\r\n" + 
					"delete FROM [d3].[dbo].[ALERT_USER_ALERT_PROPS] where USER_ALERT_ID > 3\r\n" +
					"delete from [ALERT_USER_ALERT] where NOT USER_ID = 100\r\n" + 
					"delete from [TRANSIENT_USER_EVENT]\r\n" + 
					"delete FROM [SPLIT_CATEGORIZATION]\r\n" + 
					"delete FROM [USER_TRANSACTION_JOIN]\r\n" + 
					"delete FROM [OLB_TRANSACTION]\r\n" + 
					"delete FROM [RENAMING_RULE]\r\n" + 
					"delete FROM [USER_ACCOUNT_JOIN] where ID >15\r\n" + 
					"delete FROM [TRANSIENT_USER_EVENT]\r\n" + 
					"delete FROM [OLB_USER] where USER_ID > 101\r\n" + 
					"delete FROM [TRANSFER]\r\n" + 
					"delete FROM [OLB_ACCOUNT] where ACCOUNT_ID >15\r\n" + 
					"delete FROM [BRANCH] where ID > 2\r\n" + 
					"delete FROM [STORED_CONTENT] where id > 2\r\n" + 
					"delete FROM [CATEGORIZATION_RULE] where COMPANY_ID > 2\r\n" + 
					"delete FROM [CATEGORY] where COMPANY_ID > 2\r\n" + 
					"delete FROM [COMPANY_ATTRIBUTES] where COMPANY_STRUCTURE_ID > 2\r\n" + 
					"delete FROM [COMPANY_STRUCTURE] where id > 2\r\n" + 
					"delete FROM [BUSINESS]where ID>4\r\n" + 
					"delete FROM [ADMIN_USER] where USER_ID > 3\r\n" + 
					"delete FROM [PERSON]where ID > 5\r\n" + 
					"delete FROM [ADDRESS]\r\n" + 
					"delete FROM [SYSTEM_EVENT]\r\n" + 
					"delete FROM [CAMEL_MESSAGEPROCESSED]\r\n" + 
					"delete FROM [CATEGORIZATION_RULE] where GENERATED = 1	\r\n" + 
					"delete FROM [CREDENTIALS] where ID > 5\r\n" + 
					"delete from [BUDGET_CATEGORY]\r\n" + 
					"delete from [BUDGET_PERIOD] \r\n" + 
					"delete FROM [BUDGET]\r\n" + 
					"delete FROM [TERMS_OF_SERVICE_ACCEPTANCE] where NOT USER_ID = 100\r\n" + 
					"delete FROM [FINANCIAL_GOAL] where ID >13\r\n" + 
					"delete FROM [TERMS_OF_SERVICE_ACCEPTANCE] where NOT USER_ID = 100\r\n" + 
					"delete FROM [TERMS_OF_SERVICE_ACCEPTANCE]\r\n" + 
					"delete from [FINANCIAL_GOAL_ACCOUNT] where id > 13\r\n" + 
					"delete FROM [FINANCIAL_GOAL] where ID > 13\r\n" + 
					"delete FROM [API_KEY] where COMPANY_ID > 2\r\n" + 
					"delete FROM [CATEGORY] where SUBCLASS = 'USER'\r\n" + 
					"delete from [OMIT_WORD] where id > 37\r\n" + 
					"delete from [CATEGORY] where id > 164\r\n" + 
					"", true);
	  }
	  catch(Exception e) {System.out.println(e);}
  }
  
  /**
   *  CATEGORIZATION CREATE QUERY PART 
   */
  public String insertCategorizationQueryPart() 
  {
		String _insertQueryPart = "insert into [CATEGORIZATION_RULE] " +
				   "([USER_ID],[CATEGORY_ID],[COMPANY_ID],[RULE_NAME],[TRANSACTION_TYPE],[ACCOUNT_TYPE],[DESCRIPTION] " + 
				   ",[MERCHANT_CATEGORY_CODE],[AMOUNT],[GENERATED],[SELECT_COUNT] " + 
				   ",[MATCH_COUNT],[USER_COUNT],[RULE_SUBCLASS],[CREATED_DATE],[DAY_OF_MONTH],[IS_DELETED]) ";
		return _insertQueryPart;
  }
  
  /**
   *  CATEGORIZATION CREATE QUERY PART 
   */
  public String insertRenamingQueryPart() 
  {
		String _insertQueryPart = "INSERT into RENAMING_RULE (USER_ID,COMPANY_ID,RULE_NAME,OLD_DESCRIPTION,NEW_DESCRIPTION,GENERATED," + 
								  "SELECT_COUNT,MATCH_COUNT,CREATED_DATE,RULE_SUBCLASS, AMOUNT, MERCHANT_CATEGORY_CODE,DAY_OF_MONTH )";
		return _insertQueryPart;
  }
  
  /**
   * Validate system error message
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws NumberFormatException 
 * @throws InterruptedException 
   */
  public void validateSystemEventErrMsg(String _expectedStringInTheEventMsg) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		int _totalEvent = Integer.parseInt(runQuery(connectToDB(), "SELECT COUNT(*) FROM [SYSTEM_EVENT]", false));
		String _eventDesc = runQuery(connectToDB(), "SELECT EXCEPTION_DETAIL FROM [SYSTEM_EVENT]", false);
		Assert.assertTrue("No system even has been created in the DB.", (_totalEvent>0));
		Assert.assertTrue("More than one system evens have been created in the DB.", (_totalEvent==1));  
		Assert.assertTrue("The system error message content is not right. Expected string in the exception detail: ("+
				_expectedStringInTheEventMsg + ").", _eventDesc.contains(_expectedStringInTheEventMsg));
	}
  
  public void validateSystemEventErrMsg(String _expectedStringInTheEventMsg, int _numOfEvents) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
	{
		int _totalEvent = Integer.parseInt(runQuery(connectToDB(), "SELECT COUNT(*) FROM [SYSTEM_EVENT]", false));
		String _eventDesc = runQuery(connectToDB(), "SELECT EXCEPTION_DETAIL FROM [SYSTEM_EVENT]", false);
		Assert.assertTrue("Expecting " + _numOfEvents + "number of system events. Actual total system event is " + _totalEvent + ".", (_totalEvent==_numOfEvents));  
		Assert.assertTrue("The system error message content is not right. Expected string in the exception detail: ("+
				_expectedStringInTheEventMsg + ").", _eventDesc.contains(_expectedStringInTheEventMsg));
	}
  
  /**
   * Delete existing files
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws InterruptedException 
   */
  public void deleteTrackedFiles(String _folderName) throws ParserConfigurationException, SAXException, IOException, InterruptedException
	{
	  String _folderPath = folderPathWhereDropFile() + "\\" + _folderName;
	  File _filesInFolder = new File(_folderPath);
	  File[] files = _filesInFolder.listFiles();
	  int _loop = 0;
	  while (files.length>0)
	  {
		  for(int i = 0; i < files.length; i ++)
		  {
			  files[i].delete();
		  }
		  _loop = _loop +1;
		  if(_loop>6000)
		  {
			  break;
		  }
		  files = _filesInFolder.listFiles();
	  }
	}
  
  /**
   * Validate Conduit processing creates a file
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws InterruptedException 
   */
  public void validateFileStoredIn(String _folderName) throws ParserConfigurationException, SAXException, IOException, InterruptedException
  {
	  String _folderPath = folderPathWhereDropFile() + "\\" + _folderName;
	  File _filesInFolder = new File(_folderPath);
	  File[] files = _filesInFolder.listFiles();
	  int _waitTime = 0;
	  while (files.length==0)
	  {
		  Thread.sleep(1000);
		  _waitTime = _waitTime + 1;
		  files = _filesInFolder.listFiles();
		  if (_waitTime>4){break;}
	  }  
	  Assert.assertTrue("No file has been created in the  folder, " + _folderName + " after Conduit file process!", files.length>0);
	  Assert.assertTrue("More than one file has been created in the  folder, " + _folderName + " after Conduit file process!", files.length==1);
  }
  
  public void validateFileStoredIn(String _folderName, int _numOfFileToExpect) throws ParserConfigurationException, SAXException, IOException, InterruptedException
  {
	  Thread.sleep(4000);
	  String _folderPath = folderPathWhereDropFile() + "\\" + _folderName;
	  File _filesInFolder = new File(_folderPath);
	  File[] files = _filesInFolder.listFiles();
	  Assert.assertTrue(files.length +" file has been created in the  folder, " + _folderName + " after Conduit file process!", files.length==_numOfFileToExpect);
  }
  
  /**
   * Validate Conduit processing does not create any file
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws InterruptedException 
   */
  public void validateFileNotStoredIn(String _folderName) throws ParserConfigurationException, SAXException, IOException, InterruptedException
  {
	  String _folderPath = folderPathWhereDropFile() + "\\" + _folderName;
	  File _filesInFolder = new File(_folderPath);
	  File[] files = _filesInFolder.listFiles();
	  if  (files.length>0)
	  {
		  Thread.sleep(6000);
		  files = _filesInFolder.listFiles();
		  if  (files.length>0)
		  {
			  Thread.sleep(10000);
			  files = _filesInFolder.listFiles();
			  if  (files.length>0)
			  {
				  Thread.sleep(20000);
				  files = _filesInFolder.listFiles();
				  if  (files.length>0)
				  {
					  Thread.sleep(200000);
					  files = _filesInFolder.listFiles();
				  }
			  }
		  }
	  }
	  Assert.assertTrue("The file has been created in the  folder ("+_folderName+") after Conduit file process!", files.length==0);
  }
  
  /**
   * Validate number of records 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws NumberFormatException 
 * @throws InterruptedException 
   */
  public void validateNumOfRecords(int _numBeforeConduit, int _expectedNumOfRecordsCreated) throws NumberFormatException, ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
  {
	  List<Object> _stmt = connectToDB();
	  Thread.sleep(1000);
	  int _numAfterConduit= Integer.parseInt(runQuery(_stmt, "SELECT COUNT(*) FROM [BRANCH]", false));
	  int _loopTime = 0;
	  while (_numAfterConduit==0)
	  {
		  Thread.sleep(2000);
		  _numAfterConduit= Integer.parseInt(runQuery(_stmt, "SELECT COUNT(*) FROM [BRANCH]", false));
		  if (_loopTime> 10) {break;}
		  _loopTime = _loopTime + 1;
		  
	  }
	  Assert.assertEquals("Expected total number of records to be created: " + _expectedNumOfRecordsCreated +
			  " Actual: " + (_numAfterConduit-_numBeforeConduit), _expectedNumOfRecordsCreated, (_numAfterConduit-_numBeforeConduit));
  }
  
  /**
   * VERIFY Transaction renaming in the [USER_TRANSACTION_JOIN] table
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public void verifyRenamingRule (String _fileName, String _newDesc) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException, InterruptedException
  {
	  List<String> _transactionDirecIDs = getElementValueInConduitXMLFile(_fileName, "transaction", "", "directId");
	  List<String> _totalRenamed = new ArrayList<String>();
	  for (int i = 0; i< _transactionDirecIDs.size(); i++)
	  {
		  _totalRenamed.add(runQuery(connectToDB(), "SELECT RENAMED from [USER_TRANSACTION_JOIN] WHERE TRANSACTION_ID =" +
		  			"(SELECT ID FROM [OLB_TRANSACTION] WHERE DIRECT_ID = '" + _transactionDirecIDs.get(i) + "')", false));
	  }
	  Assert.assertEquals("Not all transactions have been applied into Renaming Rule.", _transactionDirecIDs.size(), _totalRenamed.size());
	  
	  for (int i = 0; i< _totalRenamed.size(); i++)
	  {
		  Assert.assertEquals("Renaming rule did not work!",  _newDesc, _totalRenamed.get(i));
		  System.out.println(" Renamed To: " +  _totalRenamed.get(i));
	  }
  }
  
  /**
   * Delete all Conduit files
 * @throws InterruptedException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
   */
  public void deleteConduitFiles () throws ParserConfigurationException, SAXException, IOException, InterruptedException
  {
		deleteTrackedFiles(_folderProcessedData);
		deleteTrackedFiles(_folderErrorData);
		deleteTrackedFiles(_folderFailedData);
		deleteTrackedFiles(_folderPending);
		deleteTrackedFiles(_folderDuplicate);
  }
  
  public String getProcessFolderName () 
  {
		return _folderProcessedData;
  }

  public String getErrFolderName () 
  {
		return _folderErrorData;
  }
  
  public String getFailedFolderName () 
  {
		return _folderFailedData;
  }
  
  public String getPendingFolderName () 
  {
		return _folderPending;
  }
  
  public String getDupFolderName () 
  {
		return _folderDuplicate;
  }
  
  /**
   * VERIFY record tracked in the [CAMEL_MESSAGEPROCESSED]
 * @throws InterruptedException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public void verifyDupError () throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
  {
	  int _numOfRecord = Integer.parseInt(runQuery(connectToDB(),"select COUNT(*) from [CAMEL_MESSAGEPROCESSED]", false));
	  Assert.assertTrue("No record created in the CAMEL_MESSAGEPROCESSED table!", _numOfRecord>0);
	  Assert.assertTrue("More than one record has been created in the CAMEL_MESSAGEPROCESSED table!", _numOfRecord==1);
  }
  
  /**
   * Validates transaction delete through Conduit with given transaction date range
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ParseException 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public void validateTransactionDeletionWithinDateRange (String _fileName1, String _fileName2, int _numOfAddedTransaction) throws ParserConfigurationException, SAXException, IOException, ParseException, ClassNotFoundException, SQLException, InterruptedException 
  {
	  int _total = 0;
	  Date tranDate;
	  String _tListedAsToBeDeleted = "";
	  List<Object> _stmt = connectToDB();
	  List <String> transactionPostDates = getElementValueInConduitXMLFile(_fileName1, "transaction", "postDate", "");
	  List <String> tDirectIDs = getElementValueInConduitXMLFile(_fileName1, "transaction", "", "directId");
	  List <String> tDirectIDsInFile2 = getElementValueInConduitXMLFile(_fileName2, "transaction", "", "directId");
	  SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd");
	  SimpleDateFormat formatTo = new SimpleDateFormat("dd/MM/yyyy");
	  Date beginDate = new SimpleDateFormat("dd/MM/yyyy").parse(formatTo.format(formatFrom.parse(getElementValueInConduitXMLFile(_fileName2, "z", "beginDate", "").get(0))));
	  Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(formatTo.format(formatFrom.parse(getElementValueInConduitXMLFile(_fileName2, "transactionDateRange", "endDate", "").get(0))));
	  for (int t = 0; t<transactionPostDates.size(); t++)
	  {
		  tranDate = new SimpleDateFormat("dd/MM/yyyy").parse(formatTo.format(formatFrom.parse(transactionPostDates.get(t))));
		  if (tranDate.after(beginDate) && tranDate.before(endDate) || tranDate.equals(beginDate) || tranDate.equals(endDate))
		  {
			  tranDate = new SimpleDateFormat("dd/MM/yyyy").parse(formatTo.format(formatFrom.parse(transactionPostDates.get(t))));
			  for (int t2 = 0; t2<tDirectIDsInFile2.size(); t2++)
			  { 
				  if (tDirectIDsInFile2.get(t2).equals(tDirectIDs.get(t)))
				  {
					  _tListedAsToBeDeleted = "NO";					  
				  }else{
					  _tListedAsToBeDeleted = "YES";
					  break;
				  }
			  }
			  if (tDirectIDsInFile2.size()==0){_tListedAsToBeDeleted = "YES";}
			  if (_tListedAsToBeDeleted.equals("YES"))
			  {
				  _total = _total + 1;
				  Assert.assertEquals("[OLB_TRANSACTION] record (directID=" + tDirectIDs.get(t) + 
						") have NOT been deleted!", "0", runQuery(_stmt,"SELECT COUNT(*) FROM [OLB_TRANSACTION] WHERE DIRECT_ID = '"+tDirectIDs.get(t)+"' ", false, false));
				  Assert.assertEquals("[USER_TRANSACTION_JOIN] record (OLB_TRANSACTION directID=" + tDirectIDs.get(t) + ") have NOT been deleted!", "0", runQuery(_stmt,
						"select COUNT(*) FROM [USER_TRANSACTION_JOIN] WHERE TRANSACTION_ID = (SELECT ID FROM [OLB_TRANSACTION] WHERE DIRECT_ID = '"+tDirectIDs.get(t)+"') ", false, false));
				  Assert.assertEquals("[SPLIT_CATEGORIZATION] record (OLB_TRANSACTION directID=" + tDirectIDs.get(t) + ") have NOT been deleted!", "0", runQuery(_stmt,
						"select COUNT(*) FROM [SPLIT_CATEGORIZATION] where USER_TRANSACTION_JOIN_ID = (select ID FROM [USER_TRANSACTION_JOIN] " + 
				        "WHERE TRANSACTION_ID = (SELECT ID FROM [OLB_TRANSACTION] WHERE DIRECT_ID = '"+tDirectIDs.get(t)+"'))", false, false)); 
			  }
		  }
	  }
	  Assert.assertEquals("Total expected transaction after conduit transactions delete processing is incorrect!", 
			  Integer.toString((transactionPostDates.size()-_total+_numOfAddedTransaction)), runQuery(_stmt,"SELECT COUNT(*) FROM [OLB_TRANSACTION]", false, false));
	  closeDBConnection(_stmt);
  }
  
  /**
   * Validate syncDate
 * @throws InterruptedException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public String getSystemDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
  {
	  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Date date = new Date();
	  return dateFormat.format(date);
	 
  }
  
  /**
   * Validate syncDate
 * @throws InterruptedException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws ParseException 
   */
  public void validateSyncDate(String _startDateTime, String _endDateTime) throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
  {
	  List<Object> _stmt = connectToDB();
	  String _syncDateInDB = runQuery(_stmt,"SELECT SYNC_DATE FROM [OLB_ACCOUNT] ORDER BY SYNC_DATE ASC", false, false);
	  closeDBConnection(_stmt);
	  Date _syncDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_syncDateInDB);
	  Date _beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_startDateTime);
	  Date _endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_endDateTime);
	  if (_syncDate.after(_beginDate) && _syncDate.before(_endDate) || _syncDate.equals(_beginDate) || _syncDate.equals(_endDate))
	  {
	  }else{
		  Assert.fail("Expecting syncDate to be between " + _beginDate + " and " +  _endDate + ". syncDate in the DB: " + _syncDateInDB);
	  }
  }
  
  
  /**
   * Update transaction postDate values
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws TransformerException 
 * @throws TransformerFactoryConfigurationError 
   */
  public void updateTransactionPostDate(String _fileName) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
  {
		Calendar cal = Calendar.getInstance();  
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> _postDate = getElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "");
		List<String> _tDirectID = getElementValueInConduitXMLFile(_fileName, "transaction", "", "directId");
		for (int i = 0; i<_postDate.size(); i++)
		{
			cal = Calendar.getInstance();
			switch (_tDirectID.get(i)){
			case "12345678912": 
				cal.add(Calendar.MONTH, -1);
				break;
			case "12345678913": 
				cal.add(Calendar.MONTH, -2);
				break;
			case "12345678914": 
				cal.add(Calendar.MONTH, -3);
				break;
			case "12345678915": 
				cal.add(Calendar.MONTH, -4);
				break;
			case "12345678916": 
				cal.add(Calendar.MONTH, -5);
				break;
			case "12345678917": 
				cal.add(Calendar.MONTH, -6);
				break;
			case "12345678922": 
				cal.add(Calendar.MONTH, -1);
				break;
			case "12345678923": 
				cal.add(Calendar.MONTH, -2);
				break;
			case "12345678924": 
				cal.add(Calendar.MONTH, -3);
				break;
			case "12345678925": 
				cal.add(Calendar.MONTH, -4);
				break;
			case "12345678926": 
				cal.add(Calendar.MONTH, -5);
				break;
			case "12345678927": 
				cal.add(Calendar.MONTH, -6);
				break;
			default:
				cal.add(Calendar.HOUR_OF_DAY, -i*5);
				break;
			}
			setElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "", sdf.format(cal.getTime()), i);	
		}
  }
  
  /**
   * Validate AlertDestinationList 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ClassNotFoundException 

   */
  public void validateAlertDestList (String _fileName, boolean _alertDestShouldBeCreated, int _numOfAlertsWithSameName) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
  	{
		List<Object> _stmt = connectToDB();
		int count = 0;
	  	ResultSet rs=null;
	  	String queryStr1 = null;
	  	Statement stmt = (Statement) _stmt.get(0);
	  	List<String> _userDirectID = getElementValueInConduitXMLFile(_fileName, "user", "", "directId");
	  	List<String> _names = getElementValueInConduitXMLFile(_fileName, "alertDestination", "name", "");
	  	List<String> _types = getElementValueInConduitXMLFile(_fileName, "alertDestination", "type", "");
	  	List<String> _addresses = getElementValueInConduitXMLFile(_fileName, "alertDestination", "address", "");
	  	for (int l = 0; l <_userDirectID.size(); l++)
	  	{
	  		if(_alertDestShouldBeCreated)
	    	  {	
	    		for (int u = 0; u <(_names.size()/_userDirectID.size()); u++)
	    		{
	    			//validate name
	    			queryStr1 = "select count(t1.name) from [ALERT_USER_DEST] as t1, [OLB_USER] as t2 " + 
	    						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID='"+_userDirectID.get(l)+"' and t1.NAME = '"+_names.get(count)+"'";
	    		  	rs = ((Statement) stmt).executeQuery(queryStr1);
	    		  	rs.next();
	    		    Assert.assertEquals("Total number of alerts to be created is incorrect!", true, _numOfAlertsWithSameName==Integer.parseInt(rs.getString(1)));
	    		    //validate address
	    		    if (!_addresses.get(u).equals("elementDoesNotExist"))
	    		    {
	    		    	queryStr1 = "select t1.[DEST_ADDRESS] from [ALERT_USER_DEST] as t1, [OLB_USER] as t2 " + 
	    						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID='"+_userDirectID.get(l)+"' and t1.NAME = '"+_names.get(count)+"'";
	    	  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
	    	  		  	rs.next();
	    	  		    Assert.assertEquals("Alert address is incorrect or not created!", _addresses.get(count), rs.getString(1));
	    		    }
	    		    //validate type
	    		    queryStr1 = "select t3.DEST_TYPE from [ALERT_USER_DEST] as t1, [OLB_USER] as t2, [ALERT_DEST_TYPE] as t3 " + 
	    		    		    "where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID='"+_userDirectID.get(l)+"' and t1.DEST_TYPE_ID=t3.DEST_TYPE_ID and t1.NAME = '"+_names.get(count)+"'";
	    		  	rs = ((Statement) stmt).executeQuery(queryStr1);
	    		  	rs.next();
	    		    Assert.assertEquals("Alert type is incorrect or not created!", _types.get(count), rs.getString(1));
	    		    count = count + 1;
	    		}	
	    	  }else{
	    		  	for (int u = 0; u<(_names.size()/_userDirectID.size()); u++)
	    		  	{
	    		  		if (!_names.get(u).equals("EmptyValue"))
	    		  		{
	    		  			queryStr1 = "select count(t1.name) from [ALERT_USER_DEST] as t1, [OLB_USER] as t2 " + 
	        						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID='"+_userDirectID.get(l)+"' and t1.NAME = '"+_names.get(count)+"'";
	    		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
	    	  			  	rs.next();
	    	  			  	count = count + 1;
	    	  			  	try 
	    	  			  	{
	    	  			  		Assert.assertEquals("Expected no AlertDestination to be created, but it did!", true, 0==Integer.parseInt(rs.getString(1)));
	    	  			  	}
	    	  			  	catch (SQLException e)
	    	  			  	{	
	    	  			  	}
	    		  		}
	    		  	}
	    	  }
	  	}
	  	closeDBConnection(_stmt);
  	}
  
  /**
   * Validate AlertDestinationList 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ClassNotFoundException 
   */
  public void validateMsgList (String _fileName, boolean _msgsShouldBeCreated, int _numOfMsgsWithSameSubj) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
  	{
    	List<Object> _stmt = connectToDB();
    	int count = 0;
	  	ResultSet rs=null;
	  	String queryStr1 = null;
	  	Statement stmt = (Statement) _stmt.get(0);
	  	List<String> _userDirectID = getElementValueInConduitXMLFile(_fileName, "user", "", "directId");
	  	List<String> _msgType = getElementValueInConduitXMLFile(_fileName, "message", "msgType", "");
	  	List<String> _subject = getElementValueInConduitXMLFile(_fileName, "message", "subject", "");
	  	List<String> _body = getElementValueInConduitXMLFile(_fileName, "message", "body", "");
	  	List<String> _status = getElementValueInConduitXMLFile(_fileName, "message", "status", "");
 		for (int l = 0; l <_userDirectID.size(); l++)
 		{
 			if(_msgsShouldBeCreated)
 		  	  {	
 		  		for (int u = 0; u <(_subject.size()/_userDirectID.size()); u++)
 		  		{
 		  			//validate subject
 		  			queryStr1 = "select count(t1.SUBJECT) from [OLB_USER_MESSAGE] as t1, [OLB_USER] as t2 " +
 		  					    "where t1.USER_ID =t2.USER_ID and t2.[DIRECT_ID] = '"+_userDirectID.get(l)+"' and  SUBJECT = '"+_subject.get(count)+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		    Assert.assertEquals("Total messages expected to be created is incorrect!", true, _numOfMsgsWithSameSubj==Integer.parseInt(rs.getString(1)));
 		  		    //validate msgType
 		  		    queryStr1 = "select t1.MSG_TYPE from [OLB_USER_MESSAGE] as t1, [OLB_USER] as t2 " +
 	  					    "where t1.USER_ID =t2.USER_ID and t2.[DIRECT_ID] = '"+_userDirectID.get(l)+"' and  SUBJECT = '"+_subject.get(count)+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		  	System.out.println("[MSG_TYPE]: - IN XML: "+_msgType.get(count)+" IN DB:"+ rs.getString(1));
 		  		    Assert.assertEquals("msgType is incorrect!", _msgType.get(count), rs.getString(1));
 		  		    //validate body
	 		  		 if (!_body.get(count).equals("elementDoesNotExist"))
	 		  		 {
	 		  			 queryStr1 = "select t1.BODY from [OLB_USER_MESSAGE] as t1, [OLB_USER] as t2 " +
	  	  					    "where t1.USER_ID =t2.USER_ID and t2.[DIRECT_ID] = '"+_userDirectID.get(l)+"' and  SUBJECT = '"+_subject.get(count)+"'";
	  		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
	  		  		  	rs.next();
	  		  		  	System.out.println("[BODY]: - IN XML: "+_body.get(count)+" IN DB:"+ rs.getString(1));
	  		  		    try
	  		  		    {
		  		  		    if (!_body.get(count).equals("EmptyValue"))
		  		  		    {
		  		  		    if (!_body.get(count).contains("html"))
			  		  		    {
			  		  		    	Assert.assertEquals("BODY is incorrect!", _body.get(count), rs.getString(1));
			  		  		    }
		  		  		    }
  		  		    	}
	  		  		    catch (NullPointerException e) 
	  		  		    {
		  		  		    if (!_body.get(count).equals("elementDoesNotExist"))
		  		  		    {
		  		  		    	Assert.fail();
		  		  		    }
	  		  		    }
	 		  		 }
 		  		    //validate status
	 		  		if (!_status.get(count).equals("elementDoesNotExist"))
	 		  		 {
	 		  			queryStr1 = "select t1.STATUS from [OLB_USER_MESSAGE] as t1, [OLB_USER] as t2 " +
	 	  					    "where t1.USER_ID =t2.USER_ID and t2.[DIRECT_ID] = '"+_userDirectID.get(l)+"' and  SUBJECT = '"+_subject.get(count)+"'";
	 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
	 		  		  	rs.next();
	 		  		  	System.out.println("[STATUS]: - IN XML: "+_status.get(count)+" IN DB:"+ rs.getString(1));
	 		  		    Assert.assertEquals("STATUS is incorrect!", _status.get(count), rs.getString(1));
	 		  		 }
	 		  		count = count + 1;
 		  		}	
 		  	  }else{
 		  		  	for (int u = 0; u<(_subject.size()/_userDirectID.size()); u++)
 		  		  	{
 		  		  		if (!_subject.get(u).equals("EmptyValue"))
 		  		  		{
 		  		  			queryStr1 = "select count(t1.SUBJECT) from [OLB_USER_MESSAGE] as t1, [OLB_USER] as t2 " +
 		  					    "where t1.USER_ID =t2.USER_ID and t2.[DIRECT_ID] = '"+_userDirectID.get(l)+"' and  SUBJECT = '"+_subject.get(count)+"'";
 		  		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  	  			  	rs.next();
 		  	  			  	count = count + 1;
 		  	  			  	try 
 		  	  			  	{
 		  	  			  		Assert.assertEquals("Expected no Message to be created, but it did!", true, 0==Integer.parseInt(rs.getString(1)));
 		  	  			  	}
 		  	  			  	catch (SQLException e)
 		  	  			  	{	
 		  	  			  	}
 		  		  		}
 		  		  	}
 		  	  }
 		}
	  	closeDBConnection(_stmt);
  	}
  
  
  /**
   * Validate AlertDestinationList 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ClassNotFoundException 
   */
  public void validateCategoryList (String _fileName, boolean _categoryShouldBeCreated, int _numOfCategoryWithSameCategoryLevel) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
  	{
    	List<Object> _stmt = connectToDB();
    	int count = 0;
	  	ResultSet rs=null;
	  	String queryStr1 = null;
	  	Statement stmt = (Statement) _stmt.get(0);
	  	List<String> _userDirectID = getElementValueInConduitXMLFile(_fileName, "user", "", "directId");
	  	List<String> _categoryType = getElementValueInConduitXMLFile(_fileName, "category", "categoryType", "");
	  	List<String> _categoryLevel = getElementValueInConduitXMLFile(_fileName, "category", "categoryLevel", "");
	  	List<String> _categoryGroup = getElementValueInConduitXMLFile(_fileName, "category", "categoryGroup", "");
	  	List<String> _categoryName = getElementValueInConduitXMLFile(_fileName, "category", "categoryName", "");
 		for (int l = 0; l <_userDirectID.size(); l++)
 		{
 			if(_categoryShouldBeCreated)
 		  	  {	
 		  		for (int u = 0; u <(_categoryGroup.size()/_userDirectID.size()); u++)
 		  		{
 		  			//validate categoryGroup
 		  			queryStr1 = "select count(t1.CATEGORY_GROUP) from [CATEGORY] as t1, [OLB_USER] as t2 " + 
 		  						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID = '"+_userDirectID.get(l)+"' and t1.CATEGORY_GROUP = '"+_categoryGroup.get(count)+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		    Assert.assertEquals("CATEGORY_GROUP is incorrect!", true, _numOfCategoryWithSameCategoryLevel==Integer.parseInt(rs.getString(1)));
 		  		    //validate categoryType
 		  		    queryStr1 = "select t1.[CATEGORY_TYPE] from [CATEGORY] as t1, [OLB_USER] as t2 " + 
	  						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID = '"+_userDirectID.get(l)+"' and t1.CATEGORY_GROUP = '"+_categoryGroup.get(count)+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		  	System.out.println("[CATEGORY_TYPE]: - IN XML: "+_categoryType.get(count)+" IN DB:"+ rs.getString(1));
 		  		    Assert.assertEquals("CATEGORY_TYPE is incorrect!", _categoryType.get(count), rs.getString(1));
 		  		    //validate categoryLevel
 		  		    queryStr1 = "select t1.[SUBCLASS] from [CATEGORY] as t1, [OLB_USER] as t2 " + 
	  						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID = '"+_userDirectID.get(l)+"' and t1.CATEGORY_GROUP = '"+_categoryGroup.get(count)+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		  	System.out.println("[SUBCLASS]: - IN XML: "+_categoryLevel.get(count)+" IN DB:"+ rs.getString(1));
 		  		  	if (_categoryLevel.get(count).toLowerCase().equals("fi"))
 		  		  	{
 		  		  		Assert.assertEquals("categoryLevel is incorrect!", "COMPANY", rs.getString(1));
 		  		  	}else{
 		  		  		Assert.assertEquals("categoryLevel is incorrect!", _categoryLevel.get(count), rs.getString(1));
 		  		  	}
 		  		    //validate categoryName
	 		  		 if (!_categoryName.get(count).equals("elementDoesNotExist"))
	 		  		 {
	 		  			queryStr1 = "select t1.[CATEGORY_NAME] from [CATEGORY] as t1, [OLB_USER] as t2 " + 
 		  						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID = '"+_userDirectID.get(l)+"' and t1.CATEGORY_GROUP = '"+_categoryGroup.get(count)+"'";
	  		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
	  		  		  	rs.next();
	  		  		  	System.out.println("[BODY]: - IN XML: "+_categoryName.get(count)+" IN DB:"+ rs.getString(1));
	  		  		    try
	  		  		    {
		  		  		    if (!_categoryName.get(count).equals("EmptyValue"))
		  		  		    {
		  		  		    Assert.assertEquals("categoryName is incorrect!", _categoryName.get(count), rs.getString(1));
		  		  		    }
  		  		    	}
	  		  		    catch (NullPointerException e) 
	  		  		    {
		  		  		    if (!_categoryName.get(count).equals("elementDoesNotExist"))
		  		  		    {
		  		  		    	Assert.fail();
		  		  		    }
	  		  		    }
	 		  		 }
	 		  		count = count + 1;
 		  		}	
 		  	  }else{
 		  		  	for (int u = 0; u<(_categoryGroup.size()/_userDirectID.size()); u++)
 		  		  	{
 		  		  		if (!_categoryGroup.get(u).equals("EmptyValue"))
 		  		  		{
 		  		  			queryStr1 = "select count(t1.CATEGORY_GROUP) from [CATEGORY] as t1, [OLB_USER] as t2 " + 
 		  						"where t1.USER_ID=t2.USER_ID and t2.DIRECT_ID = '"+_userDirectID.get(l)+"' and t1.CATEGORY_GROUP = '"+_categoryGroup.get(count)+"'";
 		  		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  	  			  	rs.next();
 		  	  			  	count = count + 1;
 		  	  			  	try 
 		  	  			  	{
 		  	  			  		Assert.assertEquals("Expected no Category to be created, but it did!", true, 0==Integer.parseInt(rs.getString(1)));
 		  	  			  	}
 		  	  			  	catch (SQLException e)
 		  	  			  	{	
 		  	  			  	}
 		  		  		}
 		  		  	}
 		  	  }
 		}
	  	closeDBConnection(_stmt);
  	}
  
  /**
   * Validate alertList 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ClassNotFoundException 
   */
  public void validateAlertList (String _fileName, boolean _alertShouldBeCreated) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
  	{
    	List<Object> _stmt = connectToDB();
	  	ResultSet rs=null;
	  	String queryStr1, name = null;
	  	String alertType = "";
	  	Statement stmt = (Statement) _stmt.get(0);
	  	List<String> _userDirectID = getElementValueInConduitXMLFile(_fileName, "user", "", "directId");
	  	List<String> _alertType = getElementValueInConduitXMLFile(_fileName, "alert", "alertType", "");
	  	List<String> _accountDirectId = getElementValueInConduitXMLFile(_fileName, "alert", "accountDirectId", "");
	  	List<String> _param1 = getElementValueInConduitXMLFile(_fileName, "alert", "param1", "");
	  	List<String> _value1 = getElementValueInConduitXMLFile(_fileName, "alert", "value1", "");
	  	List<String> _param2 = getElementValueInConduitXMLFile(_fileName, "alert", "param2", "");
	  	List<String> _value2 = getElementValueInConduitXMLFile(_fileName, "alert", "value2", "");
	  	List<String> _param3 = getElementValueInConduitXMLFile(_fileName, "alert", "param3", "");
	  	List<String> _value3 = getElementValueInConduitXMLFile(_fileName, "alert", "value3", "");
	  	List<String> _param4 = getElementValueInConduitXMLFile(_fileName, "alert", "param4", "");
	  	List<String> _value4 = getElementValueInConduitXMLFile(_fileName, "alert", "value4", "");
	  	List<String> _param5 = getElementValueInConduitXMLFile(_fileName, "alert", "param5", "");
	  	List<String> _value5 = getElementValueInConduitXMLFile(_fileName, "alert", "value5", "");
	  	List<String> _deliveryFrequency = getElementValueInConduitXMLFile(_fileName, "alert", "deliveryFrequency", "");
	  	List<String> _ignoreDnd = getElementValueInConduitXMLFile(_fileName, "alert", "ignoreDnd", "");
	  	List<String> _externalId = getElementValueInConduitXMLFile(_fileName, "alert", "externalId", "");
	  	List<String> _alertDestinationExternalId = getElementValueInConduitXMLFile(_fileName, "alertDestination", "externalId", "");
 		for (int l = 0; l <_userDirectID.size(); l++)
 		{
 			if(_alertShouldBeCreated)
 		  	  {	
 		  		for (int u = 0; u <_alertType.size(); u++)
 		  		{
 		  	  		for (int j = 0; j <_alertDestinationExternalId.size(); j++)
 		  	  		{
 		  	  			if(_alertDestinationExternalId.get(j).equals(_externalId.get(u)))
 		  	  			{
 		  	  				name = getElementValueInConduitXMLFile(_fileName, "alertDestination", "name", "").get(0);
 		  	  				break;
 		  	  			}else{
 		  	  				Assert.fail("Could not find alertDestination externalId = "+ _externalId.get(u).toUpperCase());
 		  	  			}
 		  	  		}

 		  	  		//validate ALERT_TYPE
 		  	  		if (_alertType.get(u).equals("alerttype.check.number.cleared"))
 		  	  		{
 		  	  			alertType = "check.cleared";
 		  	  		}else{
 		  	  			alertType = _alertType.get(u).replace("alerttype.", "");
 		  	  		}
 		  			queryStr1 = "select count(t2.ALERT_TYPE) from [ALERT_USER_ALERT] as t1,  [ALERT_DEFINITION] t2, [OLB_USER] t3\r\n" + 
 		  						"where t1.ALERT_ID=t2.ALERT_ID and t1.USER_ID=t3.USER_ID and t3.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.ALERT_TYPE = '"+alertType+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		    Assert.assertEquals("Check alert type! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
 		  	  		
 		  		    //validate alertDestinationList
 		  			queryStr1 = "select count(t2.NAME) from [ALERT_USER_ALERT] as t1,  [ALERT_USER_DEST] t2, [OLB_USER] t3, [ALERT_USER_DEST_JOIN] t4\r\n" + 
	 		  					"where t3.DIRECT_ID = '"+_userDirectID.get(l)+"' and t1.USER_ID=t3.USER_ID and t1.USER_ALERT_ID=t4.USER_ALERT_ID and t4.USER_DEST_ID=t2.USER_DEST_ID and\r\n" + 
	 		  					"t2.NAME = '"+name+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		    Assert.assertEquals("Check alertDestinationList! Query:" + queryStr1, true, 0<Integer.parseInt(rs.getString(1)));
 		  	  		
 		  		    //validate accountDirectId
 		  		    if(!_accountDirectId.get(u).equals("elementDoesNotExist"))
 		  		    {
 		  		    	queryStr1 = "select count(t2.USER_ACCOUNT_ID)  from [OLB_ACCOUNT] as t1, [ALERT_USER_ALERT] as t2, \r\n" + 
		 		  		 		"  [USER_ACCOUNT_JOIN] as t4,[ALERT_DEFINITION] as t5,\r\n" + 
		 		  		 		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
		 		  		 		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t4.ID=t2.USER_ACCOUNT_ID and t1.ACCOUNT_ID=t4.ACCOUNT_ID and t1.DIRECT_ID = '"+_accountDirectId.get(u)+"' and \r\n" + 
		 		  		 		"  t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
		 		  		 		"  t8.USER_ID=t2.USER_ID and t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
		 		  		 		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
			  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
			  		  	rs.next();
			  		    Assert.assertEquals("Check alert/accountDirectId! Query:" + queryStr1, true, 0<Integer.parseInt(rs.getString(1)));
 		  		    }
	 		  		 
	 		  		//validate deliveryFrequency
	 		  		 if(!_deliveryFrequency.get(u).equals("elementDoesNotExist"))
			  		    {
		 		  			queryStr1 = "select count(t2.USER_ACCOUNT_ID)  from  [ALERT_USER_ALERT] as t2, [ALERT_DEFINITION] as t5,\r\n" + 
			 		  		 		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
			 		  		 		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
			 		  		 		"  t2.DELIVERY_FREQUENCY='"+_deliveryFrequency.get(u)+"' and t8.USER_ID=t2.USER_ID and \r\n" + 
			 		  		 		"  t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
			 		  		 		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
				  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
				  		  	rs.next();
				  		    Assert.assertEquals("Check alert/deliveryFrequency! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
			  		    }
		  		    
	 		  		//validate ignoreDnd
		  		    int dndAction = 0;
		  		    if (_ignoreDnd.get(u).equals(true)){dndAction=1;}
		  		    	
		  		    queryStr1 = "select count(t2.USER_ACCOUNT_ID)  from  [ALERT_USER_ALERT] as t2, [ALERT_DEFINITION] as t5,\r\n" + 
	 		  		 		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
	 		  		 		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
	 		  		 		"  t2.DND_ACTION='"+dndAction+"' and t8.USER_ID=t2.USER_ID and \r\n" + 
	 		  		 		"  t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
	 		  		 		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
		  		  	rs.next();
		  		    Assert.assertEquals("Check alert/ignoreDnd! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
		  		    
	 		  		//validate param1/value1
			  		  if(!_param1.get(u).equals("elementDoesNotExist"))
			  		    {
				  			queryStr1 = "select count(t2.USER_ALERT_ID) from [ALERT_USER_ALERT] as t2, [ALERT_USER_ALERT_PROPS] as t3,[ALERT_DEFINITION] as t5,\r\n" + 
				  		    		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
				  		    		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.USER_ALERT_ID=t3.USER_ALERT_ID and t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
				  		    		"  t3.NAME = '"+_param1.get(u)+"' and t3.VALUE = '"+_value1.get(u)+"' and\r\n" + 
				  		    		"  t8.USER_ID=t2.USER_ID and t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
				  		    		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
				  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
				  		  	rs.next();
				  		    Assert.assertEquals("Check param1/value1! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
			  		    }
		  		    
		  		    
			  		//validate param2/value2
			  		if(!_param2.get(u).equals("elementDoesNotExist"))
		  		    {
			  			queryStr1 = "select count(t2.USER_ALERT_ID) from [ALERT_USER_ALERT] as t2, [ALERT_USER_ALERT_PROPS] as t3,[ALERT_DEFINITION] as t5,\r\n" + 
			  		    		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
			  		    		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.USER_ALERT_ID=t3.USER_ALERT_ID and t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
			  		    		"  t3.NAME = '"+_param2.get(u)+"' and t3.VALUE = '"+_value2.get(u)+"' and\r\n" + 
			  		    		"  t8.USER_ID=t2.USER_ID and t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
			  		    		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
			  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
			  		  	rs.next();
			  		    Assert.assertEquals("Check param2/value2! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));	
		  		    }
		  		    
			  		//validate param3/value3
			  		  if(!_param3.get(u).equals("elementDoesNotExist"))
			  		    {
				  			queryStr1 = "select count(t2.USER_ALERT_ID) from [ALERT_USER_ALERT] as t2, [ALERT_USER_ALERT_PROPS] as t3,[ALERT_DEFINITION] as t5,\r\n" + 
				  		    		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
				  		    		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.USER_ALERT_ID=t3.USER_ALERT_ID and t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
				  		    		"  t3.NAME = '"+_param3.get(u)+"' and t3.VALUE = '"+_value3.get(u)+"' and\r\n" + 
				  		    		"  t8.USER_ID=t2.USER_ID and t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
				  		    		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
				  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
				  		  	rs.next();
				  		    Assert.assertEquals("Check param3/value3! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
			  		    }
			  		  
		  		    //validate param4/value4
			  		  if(!_param4.get(u).equals("elementDoesNotExist"))
			  		    {
				  			queryStr1 = "select count(t2.USER_ALERT_ID) from [ALERT_USER_ALERT] as t2, [ALERT_USER_ALERT_PROPS] as t3,[ALERT_DEFINITION] as t5,\r\n" + 
				  		    		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
				  		    		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.USER_ALERT_ID=t3.USER_ALERT_ID and t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
				  		    		"  t3.NAME = '"+_param4.get(u)+"' and t3.VALUE = '"+_value4.get(u)+"' and\r\n" + 
				  		    		"  t8.USER_ID=t2.USER_ID and t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
				  		    		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
				  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
				  		  	rs.next();
				  		    Assert.assertEquals("Check param4/value4! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
			  		    }
			  		  
			  		//validate param5/value5
			  		  if(!_param5.get(u).equals("elementDoesNotExist"))
			  		    {
				  			queryStr1 = "select count(t2.USER_ALERT_ID) from [ALERT_USER_ALERT] as t2, [ALERT_USER_ALERT_PROPS] as t3,[ALERT_DEFINITION] as t5,\r\n" + 
				  		    		"  [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8\r\n" + 
				  		    		"  where t8.DIRECT_ID = '"+_userDirectID.get(l)+"' and t2.USER_ALERT_ID=t3.USER_ALERT_ID and t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and \r\n" + 
				  		    		"  t3.NAME = '"+_param5.get(u)+"' and t3.VALUE = '"+_value5.get(u)+"' and\r\n" + 
				  		    		"  t8.USER_ID=t2.USER_ID and t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'\r\n" + 
				  		    		"  and t7.USER_DEST_ID=t6.USER_DEST_ID";
				  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
				  		  	rs.next();
				  		    Assert.assertEquals("Check param5/value5! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
			  		    }  
 		  		}	
 		  	  }else{
 	 		  		for (int u = 0; u <_alertType.size(); u++)
 	 		  		{
	 	 		  		if (_alertType.get(u).equals("alerttype.check.number.cleared"))
	 		  	  		{
	 		  	  			alertType = "check.cleared";
	 		  	  		}else{
	 		  	  			alertType = _alertType.get(u).replace("alerttype.", "");
	 		  	  		}
			  	  		queryStr1 = "select count(t2.USER_ACCOUNT_ID)  from [OLB_ACCOUNT] as t1, [ALERT_USER_ALERT] as t2, [USER_ACCOUNT_JOIN] as t4," + 
			  	  				"   [ALERT_DEFINITION] as t5, [ALERT_USER_DEST] as t6, [ALERT_USER_DEST_JOIN] as t7, [OLB_USER] as t8" + 
			  	  				"   where t4.ID=t2.USER_ACCOUNT_ID and t1.ACCOUNT_ID=t4.ACCOUNT_ID and t1.DIRECT_ID = '"+_accountDirectId.get(u)+"' and" + 
			  	  				"   t2.ALERT_ID=t5.ALERT_ID and t5.ALERT_TYPE='"+alertType+"' and t7.USER_DEST_ID=t6.USER_DEST_ID and" + 
			  	  				"   t8.USER_ID=t2.USER_ID and t2.USER_ALERT_ID=t7.USER_ALERT_ID and t6.NAME = '"+name+"'";
			  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
			  		  	rs.next();
			  		    Assert.assertEquals("The alert has been created (not expecting to be created). Here is the query: " + queryStr1, true, 0==Integer.parseInt(rs.getString(1)));
 	 		  	  	}
 		  	  }
 		}
	  	closeDBConnection(_stmt);
  	}
  
  /**
   * Validate categorizationMappingList 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ClassNotFoundException 
   */
  public void validateCategorizationMappingList (String _fileName, boolean _shouldBeCategorized) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
  	{
    	List<Object> _stmt = connectToDB();
	  	ResultSet rs=null;
	  	String queryStr1, categoryName = null;
	  	Statement stmt = (Statement) _stmt.get(0);
	  	List<String> _userDirectID = getElementValueInConduitXMLFile(_fileName, "user", "", "directId");
	  	List<String> _categoryExternalId = getElementValueInConduitXMLFile(_fileName, "categorizationMapping", "categoryExternalId", "");
	  	List<String> _externalId = getElementValueInConduitXMLFile(_fileName, "category", "externalId", "");
	  	List<String> _categoryName = getElementValueInConduitXMLFile(_fileName, "category", "categoryName", "");
	  	List<String> _transactionDirectId = getElementValueInConduitXMLFile(_fileName, "categorizationTransactionList", "transactionDirectId", "");
	  	List<String> _splitTransactionDirectId = getElementValueInConduitXMLFile(_fileName, "categorizationTransactionList", "splitTransactionDirectId", "");
 		for (int l = 0; l <_userDirectID.size(); l++)
 		{
 			if(_shouldBeCategorized)
 		  	  {	
	 		  		for (int u = 0; u <_transactionDirectId.size(); u++)
	 		  		{	 		  			
	 		  			for (int c = 0; c <_externalId.size(); c++)
	 		  			{
	 		  				if(_externalId.get(c).equals(_categoryExternalId.get(u)))
	 		  				{
	 		  					categoryName = _categoryName.get(c);
	 		  					break;
	 		  				}
	 		  			}
	 		  			//validate transactionDirectId category
	 		  			if (!_transactionDirectId.get(u).equals("elementDoesNotExist"))
	 		  			{
	 		  				queryStr1 = "select t3.DIRECT_ID as trDirectId from [SPLIT_CATEGORIZATION] as t1, [CATEGORY] as t2, [OLB_TRANSACTION] as t3, [USER_TRANSACTION_JOIN] as t4, [OLB_USER] as t5\r\n" + 
		 		  					"  where t4.USER_ID = t5.USER_ID and t5.DIRECT_ID='"+_userDirectID.get(l)+"' and t1.USER_TRANSACTION_JOIN_ID=t4.ID and t4.TRANSACTION_ID=t3.ID and t3.DIRECT_ID = '"+_transactionDirectId.get(u)+"' and\r\n" + 
		 		  					"  t1.CATEGORY_ID=t2.Id and t2.CATEGORY_NAME = '"+categoryName+"'";
		 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
			 		  		 while(rs.next()){
			 		  			 Assert.assertEquals("Categorization did not work! Query:" + queryStr1, _transactionDirectId.get(u), rs.getString("trDirectId"));
			 		          }		
	 		  			}
	 		  			
	 		  		}
	 		  		for (int u = 0; u <_splitTransactionDirectId.size(); u++){
	 		  		//validate transactionSplitId category
	 		  			if (!_splitTransactionDirectId.get(u).equals("elementDoesNotExist"))
	 		  			{
	 		  				queryStr1 = "select count(t1.CATEGORY_ID) from [SPLIT_CATEGORIZATION] as t1, [CATEGORY] as t2\r\n" + 
		 		  					"  where t1.CATEGORY_ID=t2.Id and t2.CATEGORY_NAME = '"+categoryName+"' and\r\n" + 
		 		  					"  t1.DIRECT_ID = '"+_splitTransactionDirectId+"'";
		 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
		 		  		  	rs.next();
		 		  		  	Assert.assertEquals("Categorization for splitTransaction did not work! Query:" + queryStr1, true, 1==Integer.parseInt(rs.getString(1)));
	 		  			}
	 		  		}
 		  	  }else{
	 		  		//validate transactionDirectId category
	 		  		for (int u = 0; u <_transactionDirectId.size(); u++){
	 		  			for (int c = 0; c <_externalId.size(); c++)
	 		  			{
	 		  				if(_externalId.get(c).equals(_categoryExternalId.get(u)))
	 		  				{
	 		  					categoryName = _categoryName.get(c);
	 		  					break;
	 		  				}
	 		  			}
	 		  			if (!_transactionDirectId.get(u).equals("elementDoesNotExist"))
	 		  			{
	 		  				queryStr1 = "select count(t3.DIRECT_ID) from [SPLIT_CATEGORIZATION] as t1, [CATEGORY] as t2, [OLB_TRANSACTION] as t3, [USER_TRANSACTION_JOIN] as t4, [OLB_USER] as t5\r\n" + 
		 		  					"  where t4.USER_ID = t5.USER_ID and t5.DIRECT_ID='"+_userDirectID.get(l)+"' and t1.USER_TRANSACTION_JOIN_ID=t4.ID and t4.TRANSACTION_ID=t3.ID and t3.DIRECT_ID = '"+_transactionDirectId.get(u)+"' and\r\n" + 
		 		  					"  t1.CATEGORY_ID=t2.Id and t2.CATEGORY_NAME = '"+_categoryName+"'";
				  			rs = ((Statement) stmt).executeQuery(queryStr1);
				  		  	rs.next();
				  		  	try {Assert.assertEquals("Categorization work, expecting it should not be! Query:" + queryStr1, true, 0==Integer.parseInt(rs.getString(1)));}
				  		  	catch (SQLException e) {}
	 		  			}
	 		  		}
	 		  		for (int u = 0; u <_splitTransactionDirectId.size(); u++){
	 		  		//validate transactionSplitId category
	 		  			if (!_splitTransactionDirectId.get(u).equals("elementDoesNotExist"))
	 		  			{
	 		  				queryStr1 = "select count(t1.CATEGORY_ID) from [SPLIT_CATEGORIZATION] as t1, [CATEGORY] as t2\r\n" + 
		 		  					"  where t1.CATEGORY_ID=t2.Id and" + 
		 		  					"  t1.DIRECT_ID = '"+_splitTransactionDirectId+"'";
				  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
				  		  	rs.next();
				  		  	try {Assert.assertEquals("Categorization work, expecting it should not be! Query:" + queryStr1, true, 0==Integer.parseInt(rs.getString(1)));}
				  		  	catch (SQLException e) {}	
	 		  			}
			  			
	 		  		}
	 		  		 
 		  	  }
 		}
	  	closeDBConnection(_stmt);
  	}
  
  
  /**
   * Validate splitTransaction 
 * @throws InterruptedException 
 * @throws SQLException 
 * @throws IOException 
 * @throws SAXException 
 * @throws ParserConfigurationException 
 * @throws ClassNotFoundException 
   */
  public void validateSplitTransaction (String _fileName, boolean _shouldSplitOccur) throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException
  	{
    	List<Object> _stmt = connectToDB();
	  	ResultSet rs=null;
	  	String queryStr1 = null;
	  	Statement stmt = (Statement) _stmt.get(0);
	  	List<String> _userDirectID = getElementValueInConduitXMLFile(_fileName, "user", "", "directId");
	  	List<String> _splitTransaction = getElementValueInConduitXMLFile(_fileName, "splitTransaction", "", "directId");
	  	List<String> _amount = getElementValueInConduitXMLFile(_fileName, "splitTransaction", "amount", "");
	  	

 		for (int l = 0; l <_userDirectID.size(); l++)
 		{
 			if(_shouldSplitOccur)
 		  	  {	
 				for (int s = 0; s <_splitTransaction.size(); s++)
 		 		{
 					//validate splitTransaction /amount

	  				queryStr1 = "select t1.SPLIT_AMOUNT as amount from [SPLIT_CATEGORIZATION] as t1, [USER_TRANSACTION_JOIN] as t2, [OLB_USER] as t3\r\n" + 
	  						"   where t1.USER_TRANSACTION_JOIN_ID=t2.ID and t2.USER_ID=t3.USER_ID and t3.DIRECT_ID = '"+_userDirectID.get(l)+"' and \r\n" + 
	  						"   t1.DIRECT_ID = '"+_splitTransaction.get(s)+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		  	Assert.assertEquals("Split transaction did not get created! Query:" + queryStr1, _amount.get(s), rs.getString(1));
 		  		  	System.out.println("splitTransaction: - IN XML: "+_amount.get(s)+" IN DB: "+rs.getString(1));
 		 		}
 					
 		  	  }else{
 		  		for (int s = 0; s <_splitTransaction.size(); s++)
 		 		{
 					//validate splitTransaction /amount

	  				queryStr1 = "select count(t1.SPLIT_AMOUNT) from [SPLIT_CATEGORIZATION] as t1, [USER_TRANSACTION_JOIN] as t2, [OLB_USER] as t3\r\n" + 
	  						"   where t1.USER_TRANSACTION_JOIN_ID=t2.ID and t2.USER_ID=t3.USER_ID and t3.DIRECT_ID = '"+_userDirectID.get(l)+"' and \r\n" + 
	  						"   t1.DIRECT_ID = '"+_splitTransaction.get(s)+"'";
 		  		  	rs = ((Statement) stmt).executeQuery(queryStr1);
 		  		  	rs.next();
 		  		  	Assert.assertEquals("did not expect split transactions to be created, but it did! Query:" + queryStr1, true, 0==Integer.parseInt(rs.getString(1)));
 		 		}
 					
	 		  		 
 		  	  }
 		}
	  	closeDBConnection(_stmt);
  	}
  
  
  
  
  
  
  
  
}

