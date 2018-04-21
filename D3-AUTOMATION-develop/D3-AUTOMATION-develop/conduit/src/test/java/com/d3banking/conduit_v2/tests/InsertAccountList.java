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
public class InsertAccountList extends ConduitLibrary{
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-01-1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkNumOfBusProdComp(fileName, 2, 2, 2);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		deleteConduitFiles();
    }
	
	@Test
    public void create_allElementsWithValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-01.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
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
    }
	
	@Test
    public void create_missingUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-02.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("property: uid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingCUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-03.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("property: cuid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_nonExistenceCUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-04.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("error.company.not.found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingProdUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-05.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("[property: produid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_nonExistProdUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-06.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("error.account.product.not.found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingAcctName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-07.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("property: nm; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingRttn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-08.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("property: rttn; value");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingAcctNum() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-09.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		validateSystemEventErrMsg("property: nbr; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingAcctAttrName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-10.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: attrs[0].n; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingTxnUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-11.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: uid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingTxnType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-12.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: tp; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingTxnAmount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-13.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: am; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingTxnPostDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-14.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: pd; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingTxnPending() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-15.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: pn; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingTxnPostingSeq() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-16.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: ps; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_missingTxnName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-17.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: nm; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_acctUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-18.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct  " + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("constraint: must match \"[a-zA-Z0-9\\.\\-_]{1,63}");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_AcctName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-19.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("[property: nm; value");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_Rttn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-20.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: rttn; value");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_AcctNum() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-21.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: nbr; value");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_AcctAttrName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-22.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: attrs[0].n; value");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_txnUID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-23.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn   " + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("must match \"[a-zA-Z0-9\\.\\-_]{1,31}");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_txnType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-24.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: tp; value:  ; constraint: must match \"[cC]|credit|[dD]|debit\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_txnAmount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-25.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: am; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_txnPostDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-26.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: pd; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_txnPending() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-27.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: pn; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_spaceValueFor_txnPostingSeq() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-28.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);  //DDB-841
    }
	
	@Test
    public void create_spaceValueFor_txnName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-29.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: nm; value:   ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidAcctStatus() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-30.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 0);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: sts; value: t; constraint: must match \"[oO]|open|[cC]|closed|[fF]|frozen\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_incorrectFormatOpenDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-31.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 1);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidTxnType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-32.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: tp; value: m; constraint: must match \"[cC]|credit|[dD]|debit");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidTxnPending() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-33.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: pn; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidPostDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-34.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("property: pd; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_invalidMCC() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-35.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 0);
		validateSystemEventErrMsg("constraint: must match \"[\\d]{4}\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_omitAccountOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-36.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 0);
		checkNumOfTxn(fileName, 0);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void create_omitAcctAttrTxnOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-37.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 1);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_outsideOfTxnDateRange() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-38.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 1);
		checkNumOfAcctAttr(fileName, 1);
		checkNumOfTxn(fileName, 1);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_multipleAcctTxn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-accountList-39.xml";
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", "acct" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", "txn" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfAcct(fileName, 4);
		checkNumOfAcctAttr(fileName, 10);
		checkNumOfTxn(fileName, 7);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); //DDB-845
    }

	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
		deleteAllTestData();
    }

}



