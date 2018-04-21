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

public class UpdateAccountList extends ConduitLibrary{
	String tDay = getTodayDate();
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, TransformerFactoryConfigurationError, TransformerException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-01-1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		deleteConduitFiles();
		
		fileName = "conduit-accountList-01.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		deleteConduitFiles();
    }
	
	@Test
    public void update_allAccountElementsWithSameValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-01.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 2);
		checkNumOfTxn(fileName, 1);
		validateAccountListElement(fileName);		
		validateFileNotStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileStoredIn(duplicateDir);
		validateSystemEventErrMsg("File was not processed"); //DDB-1874
    }
	
	@Test
    public void update_allAccountElementsWithDiffValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-40.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 2);
		checkNumOfTxn(fileName, 1);
		validateNoSystemEventCreated(); 
		validateAccountListElement(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void update_AcctBalOnly() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-41.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		validateNoSystemEventCreated(); 
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    } 
	
	@Test
    public void update_onlyTxn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-42.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfTxn(fileName, 1);
		validateNoSystemEventCreated(); 
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir); 
    }
	
	@Test
    public void update_addingNewTxn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-43.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfTxn(fileName, 1); 
		validateNoSystemEventCreated(); 
		validateAccountListElement(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void update_txnFallsOutsideTDR() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-44.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfTxn(fileName, 1);
		validateNoSystemEventCreated(); 
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    } 
	
	@Test
    public void update_addingNewTxnFallsOutsideTDR() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-45.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfTxn(fileName, 1);
		validateNoSystemEventCreated(); 
		validateAccountListElement(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }  
	
	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
		deleteAllTestData();
    }
}
