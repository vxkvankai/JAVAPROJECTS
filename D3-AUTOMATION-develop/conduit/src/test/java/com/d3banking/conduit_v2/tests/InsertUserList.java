package com.d3banking.conduit_v2.tests;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v2.functions.ConduitLibrary;

public class InsertUserList extends ConduitLibrary{
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-01-1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		deleteConduitFiles();
    }
	
	@Test
    public void create_allUserListElementsWithValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-01.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 1);
		checkNumOfPersons(fileName, 1);
		validateUserListElement(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_missingUserUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-02.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("property: uid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingUserCUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-03.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("property: cuid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidUserCUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-04.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("cuid=non-existing-cuid");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingUserLogin() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-05.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("property: login; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingUserTaxIdType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-06.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("txtp required for txid");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingUserPersonElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-07.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("propertyPath=person");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingUserPersonLastName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-08.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.ln; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonPhysAdd1() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-19.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.paddr.a1; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonPhysCity() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-20.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.paddr.ct; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonPhysState() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-21.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.paddr.st; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonPhysPostalCode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-22.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.paddr.pc; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonMailAdd1() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-23.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.maddr.a1; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonMailCity() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-24.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.maddr.ct; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonMailState() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-25.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.maddr.st; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingPersonMailPostalCode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-26.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.maddr.pc; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spacesFor_userUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-09.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr  " + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("constraint: must match \"[a-zA-Z0-9\\.\\-_]{1,63}");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
//	@Test
//    public void create_spacesFor_userLogin() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
//    {
//		String path = folderPathWhereDropFile();
//		String fileName = "conduit-userList-10.xml";
//		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
//		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l  " + getTodayDate());
//		dropConduitTestFile(fileName, path);
//		waitConduitFileProcessing(fileName, path, 120);
//		checkNumOfUsers(fileName, 0);            //DDB-2384      
//		checkNumOfUserAcct(fileName, 0);
//		validateSystemEventErrMsg("property: login; value:     ; constraint: may not be empty");
//		validateFileStoredIn(proccessedDir);
//		validateFileNotStoredIn(failedDir);
//		validateFileStoredIn(errorDir);
//    }
	
	@Test
    public void create_spacesFor_taxIdType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-11.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		validateSystemEventErrMsg("constraint: must match \"[sS]|ssn|[tT]|tin|[iI]|international\"; ]");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spacesFor_personLastName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-12.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: person.ln; value:     ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidGender() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-13.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("[property: g; value: x; constraint: must match \"[mM]|male|[fF]|female\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
/*	@Test
    public void create_invalidBillpayStatus() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-14.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 0);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 0);
		validateSystemEventErrMsg("property: bpsts; value: n; constraint: must match \"[aA]|active|[fF]|frozen|[iI]|inactive\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir); //DDB-1877
    }*/
	
	@Test
    public void create_invalidCreditScoreDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-15.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 1);
		checkNumOfPersons(fileName, 1);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_omitUserPersonOptionals() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-16.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 1);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void create_invalidAUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-17.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 1);
		validateSystemEventErrMsg("error.user.associate.account.not.found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidUserAcctListAction() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-18.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 0);
		checkNumOfPersons(fileName, 1);
		validateSystemEventErrMsg("property: a; value: invalid; constraint: must match \"[aA]|associate|[dD]|disassociate\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_multipleUserList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-27.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUsers(fileName, 4);
		checkNumOfUserAcct(fileName, 4);
		checkNumOfPersons(fileName, 4);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	
	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
		deleteAllTestData();
    }
}
