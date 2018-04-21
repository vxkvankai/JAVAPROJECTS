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
public class UpdateCompanyList extends ConduitLibrary{
	String compUid = "comp" + getTodayDate();
	String busnNm = "bn" + getTodayDate();
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String fileName = "conduit-companyList-18.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);	
		String path = folderPathWhereDropFile();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		deleteConduitFiles();
    }
	
	@Test
    public void update_allElementsWithSameData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-18.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 250);
		validateFileNotStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileStoredIn(duplicateDir);
		validateSystemEventErrMsg("File was not processed");
    }
	
	@Test
    public void update_allElementsWithDiffData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-33.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void update_addingNewAccProd() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-34.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkNumOfBusProdComp(fileName, 1, 1, 0);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void update_addingNewElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName1 = "conduit-companyList-35-1.xml";
		setElementValueInConduitXMLFile(fileName1, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName1, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 120);
		validateNoSystemEventCreated();
		checkNumOfBusProdComp(fileName1, 1, 0, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		deleteConduitFiles();
		
		path = folderPathWhereDropFile();
		String fileName2 = "conduit-companyList-35-2.xml";
		setElementValueInConduitXMLFile(fileName2, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName2, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 120);
		validateNoSystemEventCreated();
		checkNumOfBusProdComp(fileName2, 1, 0, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		
		deleteConduitFiles();
		path = folderPathWhereDropFile();
		String fileName3 = "conduit-companyList-35-3.xml";
		setElementValueInConduitXMLFile(fileName3, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName3, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName3, path);
		waitConduitFileProcessing(fileName3, path, 120);
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



