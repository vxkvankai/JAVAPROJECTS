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

public class InsertAllLists extends ConduitLibrary{
	
	String comp = "comp" + getTodayDate();
	String acct = "acct" + getTodayDate();
	String alertdest = "alertdest" + getTodayDate();
	String txn = "txn" + getTodayDate();
	String cat = "cat" + getTodayDate();
	String usr = "usr" + getTodayDate();
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		deleteConduitFiles();
    }
	
	@Test
    public void create_allListElementsWithValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-01.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		
		//user migrations
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		
		checkNumOfBusProdComp(fileName, 2, 1, 2);
		
		checkNumOfAcct(fileName, 2);
		checkNumOfAcctAttr(fileName, 4);
		checkNumOfTxn(fileName, 2);
		
		checkNumOfUsers(fileName, 1);
		checkNumOfUserAcct(fileName, 2);
		checkNumOfPersons(fileName, 1);
		
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_conduitElementOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-02.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("xml not terminated properly");
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_headerElementOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-03.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("error.missing.hdr.element");
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_OptionalElementsOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-04.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_txnDateRangeOmitted_NoPresentTxns() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-05.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_txnDateRangeOmitted_presentTxnsExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-06.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_versionOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-07.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("error.missing.hdr.element");
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_requestIdOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-08.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("error.missing.hdr.element");
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void header_optionalElementsOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-09.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void autheticationElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-10.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void txnDateRange_invalidStartDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-11.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("error.missing.hdr.element"); 
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
	    validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void txnDateRange_invalidEndDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-12.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("error.missing.hdr.element");
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
	    validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void txnDateRange_endDateRecentThanStartDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-allLists-13.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("error.missing.hdr.element");
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
	    validateFileNotStoredIn(errorDir);
    }
	
	
	
	
	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
		deleteAllTestData();
    }
	
	
	
	
}
