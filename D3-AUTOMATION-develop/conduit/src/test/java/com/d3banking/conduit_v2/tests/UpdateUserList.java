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

public class UpdateUserList extends ConduitLibrary{
	String tDay = getTodayDate();
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, TransformerFactoryConfigurationError, TransformerException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-01-1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		
		fileName = "conduit-userList-01.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		deleteConduitFiles();
    }
	
	@Test
    public void update_allUserListElementsWithSameValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-01.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("Duplicate File Error");
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 1);
		checkNumOfPersons(fileName, 1);
		validateUserListElement(fileName);
		validateFileNotStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileStoredIn(duplicateDir);
    }
	
	@Test
    public void update_allUserListElementsWithDiffValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-30.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 1);
		checkNumOfPersons(fileName, 1);
		validateUserListElement(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);  //DDB-854
    }
	
	@Test
    public void update_onlyLoginId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-31.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "lu" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkNumOfUsers(fileName, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);  
    }
	
	@Test
    public void update_onlyLastName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-32.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkNumOfUsers(fileName, 1);
		checkNumOfPersons(fileName, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void update_onlyPhysRequiredElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-33.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkNumOfUsers(fileName, 1);
		checkNumOfPersons(fileName, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void update_disassociateAcct() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-34.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkAccUserAssocation(fileName);
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

