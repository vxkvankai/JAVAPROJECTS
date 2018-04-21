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

public class DeleteUserList extends ConduitLibrary{
	String tDay = getTodayDate();
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, TransformerFactoryConfigurationError, TransformerException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-remove-01-1.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		deleteConduitFiles();
    }
	
	@Test
    public void delete_userExist_setDeletionFlagToTrueFalse() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-remove-01.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		//validateNoSystemEventCreated(); //DDB-1691
		checkNumOfUsers(fileName, 3);
		checkNumOfPersons(fileName, 3);
		checkUserDeletionSet(fileName, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		//validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
		
		fileName = "conduit-userList-remove-02.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		//validateNoSystemEventCreated(); //DDB-1691
		checkNumOfUsers(fileName, 3);
		checkNumOfPersons(fileName, 3);
		checkUserDeletionSet(fileName, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		//validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nonExistentUser_setDeletionFlagToTrue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-remove-03.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr2" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l2" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkUserDeletionSet(fileName, false);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyUserRequiredElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-remove-05.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: person.ln; value: null; constraint: may not be empty");
		validateSystemEventErrMsg("login; value: ; constraint: size must be between 1 and 31");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyPersonRequiredElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-remove-07.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: person.ln; value: ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyMAddressElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-remove-09.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: person.maddr.st; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: person.maddr.pc; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: person.maddr.a1; value: ; constraint: size must be between 1 and 255");
		validateSystemEventErrMsg("person.maddr.ct; value");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyPAddressElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userList-remove-09.xml";
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", "usr" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: person.maddr.st; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: person.maddr.pc; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: person.maddr.a1; value: ; constraint: size must be between 1 and 255");
		validateSystemEventErrMsg("property: person.maddr.ct; value: ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
		deleteAllTestData();
    }
}
