package com.d3banking.conduit_v2.tests;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v2.functions.ConduitLibrary;


@RunWith(JUnit4.class)
public class InsertCompanyList extends ConduitLibrary{
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		deleteConduitFiles();
    }
	
	@Test
    public void create_missingCompanyListElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		
		 String path = folderPathWhereDropFile(); 
		 String fileName ="conduit-companyList-01.xml";
		 dropConduitTestFile(fileName, path);
		 waitConduitFileProcessing(fileName, path, 120);
		 validateSystemEventErrMsg("error in pi: [xx][mm][ll] not a valid pi targetname");
		 validateFileNotStoredIn(proccessedDir);
		 validateFileStoredIn(failedDir);
		 validateFileNotStoredIn(errorDir);
     }
	
	@Test
    public void create_omittedCompanyUidAttribute() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-02.xml";
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("CompanyProcessorImpl");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedCompanyTypeElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-03.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: tp; value: null; ");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir); 
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedCompanyBusinessElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-04.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("INVALIDBUSINESSTYPE");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedCompanyBusinessNameElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-05.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.nm; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedBusinessPhysAddress1Element() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-19.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.paddr.a1; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedBusinessMailingAddress1Element() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-20.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.maddr.a1; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedBusinessPhysCityElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-21.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.paddr.ct; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedBusinessMailingCityElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-22.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.maddr.ct; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedBusinessPhysStateElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-23.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.paddr.st; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedBusinessMailingStateElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-24.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.maddr.st; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }

	@Test
    public void create_omittedBusinessPhysPostalCodeElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-25.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.paddr.pc; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedBusinessMailingPostalCodeElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-26.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.maddr.pc; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedContact1LastName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-27.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.ct1.ln; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedContact2LastName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-28.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: busn.ct2.ln; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedCompanyAttributeName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-29.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: attrs[0].n; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedCompanyAttributeValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-30.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedAccProdAttributeName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-31.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateSystemEventErrMsg("property: attrs[0].n; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedAccProdAttributeValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-32.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkNumOfBusProdComp(fileName, 1, 1, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedAccProductUidAttribute() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-06.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateSystemEventErrMsg("property: uid; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedAccProductNameElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-07.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateSystemEventErrMsg("property: nm; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedAccClassificationElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-08.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateSystemEventErrMsg("property: ac; value: null; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }

	@Test
    public void create_emptyCompanyUidAttribute() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-09.xml";
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("property: uid; value:  ; constraint: must match \"[a-zA-Z0-9\\.\\-_]{1,63}");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir); 
    }
	
	@Test
    public void create_emptyCompanyTypeElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-10.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		//validateSystemEventErrMsg("must match \"root|institution|processor|holding|region|branch\""); //DDB-1810
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir); 
		validateFileStoredIn(errorDir);
    }
	
	@Test
	public void create_emptyCompanyBusinessNameElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-11.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_emptyAccProductUidAttribute() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-12.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateSystemEventErrMsg("property: uid; value:    ; constraint: must match \"[a-za-z0-9\\.\\-_]{1,63}\"; ]");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_emptyAccProductNameElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-13.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateSystemEventErrMsg("property: nm; value:     ; constraint: may not be empty;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_emptyAccProdAccClassificationElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-14.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateSystemEventErrMsg("must match \"[aa]|asset|[ll]|liability\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omittedAccProdListElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-15.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateCompanyListElement(fileName);
		validateNoSystemEventCreated();
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);  // DDB-794
    }
	
	@Test
    public void create_omittedAllOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-16.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkNumOfBusProdComp(fileName, 1, 1, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void create_invalidBusinessType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-17.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfBusProdComp(fileName, 0, 0, 0);
		validateSystemEventErrMsg("invalidBusinessType");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_validateRegexValidationForAcctProdUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-37.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNumOfSystemEvents(30, "must match \"[a-zA-Z0-9\\.\\-_]{1,63}");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
    }
}



