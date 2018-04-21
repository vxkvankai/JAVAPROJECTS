package com.d3banking.conduit_v2.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v2.functions.ConduitLibrary;


public class DeleteAccountList extends ConduitLibrary{

	String tDay = getTodayDate();
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, TransformerFactoryConfigurationError, TransformerException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-remove-01-1.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		deleteConduitFiles();
    }
	
	@Test
    public void delete_accsExist_setDeletionFlagToTrueFalse() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "conduit-accountList-remove-01.xml";
		setElementValueInConduitXMLFile(fileName1, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName1, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 120);
		validateNoSystemEventCreated(); 
		checkAccDeletionSet(fileName1, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
		deleteConduitFiles();

		String fileName2 = "conduit-accountList-remove-02.xml";
		setElementValueInConduitXMLFile(fileName2, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName2, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 120);
		validateNoSystemEventCreated(); 
		checkAccDeletionSet(fileName2, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nonExistentAcc_setDeletionFlagToTrue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-remove-03.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkAccDeletionSet(fileName, false);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_withinTDR_pendingTxnExistInDbNotInFile() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "conduit-accountList-remove-01-1.xml";
		String fileName2 = "conduit-accountList-remove-04.xml";
		setElementValueInConduitXMLFile(fileName1, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName1, "txn", "", "uid", "txn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName2, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName2, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 120);
		validateNoSystemEventCreated(); 
		checkTxnsDeletion(fileName1, fileName2);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }

	@Test
    public void delete_accOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-remove-05.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkAccountListElementsNullify(fileName);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 2);
		checkNumOfTxn(fileName, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
		deleteConduitFiles();
    }
	
	@Test
    public void delete_accRequiredElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-remove-06.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: rttn; value: ; constraint: size must be between 9 and 9");
		validateSystemEventErrMsg("property: nm; value: ; constraint: size must be between 1 and 127");
		validateSystemEventErrMsg("property: nbr; value: ; constraint: size must be between 1 and 200");
		validateSystemEventErrMsg("property: produid; value: ; constraint: size must be between 1 and 31");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }

	@Test
    public void delete_txnOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-remove-07.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkAccountListElementsNullify(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_tnxRequiredElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-remove-08.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: nm; value: ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_acctAltElementValuesNullify() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-remove-09.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + tDay);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + tDay);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkAccountListElementsNullify(fileName);
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
