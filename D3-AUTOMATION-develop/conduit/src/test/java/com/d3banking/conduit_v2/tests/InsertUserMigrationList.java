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

public class InsertUserMigrationList extends ConduitLibrary{
	String tDay = getTodayDate();
	String comp = "comp" + tDay;
	String acct = "acct" + tDay;
	String alertdest = "alertdest" + tDay;
	String txn = "txn" + tDay;
	String cat = "cat" + tDay;
	String usr = "usr" + tDay;
	
//	Currently (6/16/14) conduit does not support the following alert types:
//	•	Failed Transfer Alert (transfer.failure)
//	•	Successful Transfer Alert (transfer.success)
	
	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, TransformerFactoryConfigurationError, TransformerException 
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-01-1.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", comp);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "bn" + tDay);
		setElementValueInConduitXMLFile(fileName, "usr", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "usrlst", "usr", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "usr", "login", "", "l" + tDay);
		setElementValueInConduitXMLFile(fileName, "acctlst", "acct", "", "cuid", comp, 2);
		setElementValueInConduitXMLFile(fileName, "acct", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "usracctlist", "usracct", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "txn", "", "uid", txn);
		setElementValueInConduitXMLFile(fileName, "complst", "comp", "", "puid", comp, 1, 0, 1, 0);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		deleteConduitFiles();
    }
	
	@Test
    public void create_allUserMigrationListAllElementsWithValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-01.xml";
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
		validateNoSystemEventCreated();
		validateAccDisplay(fileName);
		validateAlertDest(fileName);
		validateAlert(fileName);
		validateMessage(fileName);
		validateCategory(fileName);
		validateCatMapping(fileName);
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 2, 2, 1, 3);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_allUserMigrationListOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-02.xml";
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
		validateSystemEventErrMsg("error.no.comp-usr-acct-usrmig.found");              
		validateFileNotStoredIn(proccessedDir);
		validateFileStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void create_userMigrationList_acctDisplayListOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-03.xml";
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
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	
	@Test
    public void create_userMigrationList_alertDestListOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-04.xml";
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
		validateNoSystemEventCreated();
		validateAlertDest(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertListOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-05.xml";
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
		validateNoSystemEventCreated();
		validateAlertDest(fileName);
		validateAlert(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_messageListOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-06.xml";
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
		validateNoSystemEventCreated();
		validateMessage(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryListOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-07.xml";
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
		validateNoSystemEventCreated();
		validateCategory(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingListOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-08.xml";
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
		validateNoSystemEventCreated();
		validateCategory(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void create_userMigrationList_uidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-09.xml";
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
		checkNumOfUserMigrationList(fileName, 0, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("error.user.not.found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_acctDisplay_uidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-10.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 2, 1, 3);
		validateSystemEventErrMsg("property: uid; value: null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_nonExistUserUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-11.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "non_exist", usr);
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
		checkNumOfUserMigrationList(fileName, 0, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("error.user.not.found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_accDisplayList_nonExistAccUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-12.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "non_exist", acct);
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
		checkNumOfUserMigrationList(fileName, 0, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("property: uid; value: null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_uidWithSpaceValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-13.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("error.user.not.found");
		checkNumOfUserMigrationList(fileName, 0, 0, 0, 0, 0, 0, 0);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_acctDisplay_uidWithSpaceValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-14.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
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
		validateSystemEventErrMsg("property: uid; value: null");
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertDestList_uidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-15.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 0, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: uid; value: null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertDestList_uidSpaceValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-16.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 0, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: uid; value:    ; constraint: must match \"[a-zA-Z0-9\\.\\-_]{1,63}\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertDestList_nameOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-17.xml";
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
		checkNumOfUserMigrationList(fileName, 0, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: nm; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertDestList_typeOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-18.xml";
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
		checkNumOfUserMigrationList(fileName, 0, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: tp; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertDestList_invalidType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-19.xml";
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
		checkNumOfUserMigrationList(fileName, 0, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: tp; value: dropbox; constraint: must match \"email|inbox\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertDestList_nameSpaceValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-20.xml";
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
		checkNumOfUserMigrationList(fileName, 0, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: nm; value:    ;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_uidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-21.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: uid; value: null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_uidSpaceValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-22.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: uid; value:      ; constraint: must match \"[a-zA-Z0-9\\.\\-_]{1,63}\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_typeOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-23.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: tp; value: null; constraint: may not be null;");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_invalidType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-24.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: tp; value: invalid.type; constraint");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_invalidFreq() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-25.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: freq; value: invalidfreq; constraint");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_invalidDnd() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-26.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("property: dnd; value: invaliddnd");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_nonExistAlertDest() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-27.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("No AlertDestinations found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertList_spaceAlertDest() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-28.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 1, 1, 0, 0, 0);
		validateSystemEventErrMsg("No AlertDestinations found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_msgList_uidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-29.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 1, 0, 1, 0, 1, 3);
		validateSystemEventErrMsg("msg: uid=null errors");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_msgList_uidWithSpaces() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-30.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 1, 0, 1, 0, 1, 3);
		validateSystemEventErrMsg("constraint: must match \"[a-za-z0-9\\.\\-_]{1,63}");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_msgList_typeOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-31.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 0, 1, 0, 1, 3);
		validateSystemEventErrMsg("property: tp; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_msgList_invalidType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-32.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 0, 1, 0, 1, 3);
		validateSystemEventErrMsg("invalid.type");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_msgList_subjOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-33.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 0, 1, 0, 1, 3);
		validateSystemEventErrMsg("property: sb; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_msgList_invalidStatus() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-34.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 0, 1, 0, 1, 3);
		validateSystemEventErrMsg("property: sts; value: invalid.status; constraint");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_uidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-35.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: uid; value: null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_uidWithSpaces() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-36.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: uid; value:     ; constraint: must match \"[a-zA-Z0-9\\.\\-_]{1,63}\"");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_typeOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-37.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: tp; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_invalidType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-38.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: tp; value: invalid.type; constraint: must match \"expense|income");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_levelOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-39.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: lvl; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_invalidLevel() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-40.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: lvl; value: invalid.level; constraint: must match \"user|company|system");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_groupOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-41.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: grp; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_categoryList_groupWithSpaces() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-42.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 0, 0, 1, 3);
		validateSystemEventErrMsg("property: grp; value:    ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingList_nonExistTuid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-43.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateSystemEventErrMsg("null");  //DDB-1418
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingList_nonExistAuid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-44.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateSystemEventErrMsg("null");  //DDB-1418
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingList_catTnxList_uidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-45.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateSystemEventErrMsg("category not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingList_catTnxList_uidNonExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-46.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateSystemEventErrMsg("Category not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingList_catTnxList_amountOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-47.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateSystemEventErrMsg("property: cat[0].amt; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingList_catTnxList_totalAmountNotEqualTnxAmount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-48.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateSystemEventErrMsg("Splits must equal total transaction amount");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_catMappingList_catTnxList_amountWithSpaces() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-49.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 1, 0, 1, 3);
		validateSystemEventErrMsg("property: cat[0].amt; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_multipleLists() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-50.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest, 1);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1, 0, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", cat, 1, 1, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", cat, 1, 2, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1, 5, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1, 6, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1, 11, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", cat, 1, 12, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", cat, 1, 13, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1, 16, 0, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1, 17, 0, 0);		
		setElementValueInConduitXMLFile(fileName, "acctdisplst", "acctdisp", "", "uid", acct, 1, 1, 0, 0);
		setElementValueInConduitXMLFile(fileName, "acctdisplst", "acctdisp", "", "uid", acct, 2, 1, 1, 0);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1, 22, 0, 0);	
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
	    validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);  //DDB-960
    }
	
	@Test
    public void create_userMigrationList_allInOneFile() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-51.xml";
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
		validateNoSystemEventCreated();
		validateAccDisplay(fileName);
		validateAlertDest(fileName);
		validateAlert(fileName);
		validateMessage(fileName);
		validateCategory(fileName);
		validateCatMapping(fileName);
		checkNumOfUserMigrationList(fileName, 1, 1, 1, 2, 2, 1, 3);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBalanceThreshold_nonSupportAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-53.xml";
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
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBalanceThreshold_nonExistAccUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-54.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBalanceThreshold_invalidOperatorValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-55.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: OPERATOR");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBalanceThreshold_invalidThresholdValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-56.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
//	@Test
//    public void create_userMigrationList_alertBudgetCatThreshold_nonSupportAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
//    {
//		String path = folderPathWhereDropFile();
//		String fileName = "conduit-userMigrationList-57.xml";
//		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
//		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
//		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
//		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
//		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
//		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
//		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + getTodayDate());
//		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
//		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
//		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
//		dropConduitTestFile(fileName, path);
//		waitConduitFileProcessing(fileName, path, 120);
//		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
//		validateSystemEventErrMsg("Attribute not recognized");
//		validateFileStoredIn(proccessedDir);
//		validateFileNotStoredIn(failedDir);
//		validateFileStoredIn(errorDir);   //???
//    }
	
	@Test
    public void create_userMigrationList_alertBudgetCatThreshold_nonExistCatUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-58.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Category not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBudgetCatThreshold_invalidThresholdAttrAmount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-59.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD_ATTR");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBudgetCatThreshold_invalidThresholdTypeApproach() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-60.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD_TYPE");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	@Test
    public void create_userMigrationList_alertBudgetCatThreshold_invalidThresholdValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-61.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 1, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	@Test
    public void create_userMigrationList_alertBudgetTotalThreshold_nonSupportAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-62.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBudgetTotalThreshold_invalidThresholdAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-63.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD_ATTR");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertBudgetTotalThreshold_invalidThresholdType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-64.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD_TYPE");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	@Test
    public void create_userMigrationList_alertBudgetTotalThreshold_invalidThresholdValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-65.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	@Test
    public void create_userMigrationList_alertCreditDeposit_nonSupportAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-66.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertCreditDeposit_invalidAccUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-67.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertTxnAmount_nonSupportAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-68.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_alertTxnAmount_invalidAccUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-69.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	@Test
    public void create_userMigrationList_alertTxnAmount_invalidThresholdValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-70.xml";
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
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Invalid attribute: THRESHOLD");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	@Test
    public void create_userMigrationList_alertTxnMerchant_nonSupportAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-71.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void create_userMigrationList_alertAccReminder_nonSupportAttr() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-72.xml";
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
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); 
    }
	
	@Test
    public void create_userMigrationList_alertCheckNumCleared_nonExistAccUid() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-73.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		checkNumOfUserMigrationList(fileName, 1, 0, 0, 0, 0, 0, 0);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	
	//*************************************************************scxfrlst****************************************************
	//*************************************************************scxfrlst****************************************************
	//*************************************************************scxfrlst****************************************************
	
	@Test
    public void create_userMigrationList_scxfrlst_allWithValidValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-01.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateScheduledTransfers(fileName, 24, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); //DDB-1720
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_fauidNotExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-02.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_tauidNotExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-03.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_fauid_tauid_areSame() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-04.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("fauid,tauid - cannot be equal");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir); 
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_fauidOmiited() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-05.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: fauid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_tauidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-06.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: tauid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
//	@Test
//    public void create_userMigrationList_scxfrlst_nmOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
//    {
//		String path = folderPathWhereDropFile();
//		String fileName = "conduit-userMigrationList-scxfrlst-07.xml";
//		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
//		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
//		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
//		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
//		dropConduitTestFile(fileName, path);
//		waitConduitFileProcessing(fileName, path, 120);
//		validateSystemEventErrMsg("property: nm; value: null; constraint: may not be empty");
//		validateFileStoredIn(proccessedDir);
//		validateFileNotStoredIn(failedDir);
//		validateFileStoredIn(errorDir); //DDB-1722
//    }
	
	@Test
    public void create_userMigrationList_scxfrlst_amtOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-08.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: amt; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_bdtOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-09.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: bdt; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_freqNonSupported() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-10.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: freq");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_endDateOccurIndef_All3In() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-11.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg(""); //DDB-1726
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_endDateIndefListedIn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-12.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg(""); //DDB-1726
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_endDateOccurListedIn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-13.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg(""); //DDB-1726
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_scxfrlst_indefOccurListedIn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-scxfrlst-14.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "scxfr", "", "uid", "scxfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "scxfrlst", "scxfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg(""); //DDB-1726
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	//*************************************************************xfrlst****************************************************
	//*************************************************************xfrlst****************************************************
	//*************************************************************xfrlst****************************************************
	
	@Test
    public void create_userMigrationList_xfrlst_allWithValidValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-01.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateTransfers(fileName, 3);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_optionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-02.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateTransfers(fileName, 1);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_fauidNotExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-03.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_tauidNotExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-04.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("Account not found");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_fauid_tauid_areSame() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-05.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("fauid,tauid - cannot be equal");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_fauidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-06.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: fauid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_tauidOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-07.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: tauid; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);    }
	
//	@Test
//    public void create_userMigrationList_xfrlst_nmOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
//    {
//		String path = folderPathWhereDropFile();
//		String fileName = "conduit-userMigrationList-xfrlst-08.xml";
//		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
//		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
//		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
//		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
//		dropConduitTestFile(fileName, path);
//		waitConduitFileProcessing(fileName, path, 120);
//		validateSystemEventErrMsg("property: nm; value: null; constraint: may not be empty"); //DDB-1722
//		validateFileStoredIn(proccessedDir);
//		validateFileNotStoredIn(failedDir);
//		validateFileStoredIn(errorDir);
//    }
	
	@Test
    public void create_userMigrationList_xfrlst_amtOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-09.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: amt; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_postdtOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-10.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: postdt; value: null; constraint: may not be null");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
    }
	
	@Test
    public void create_userMigrationList_xfrlst_amtNegativeAmount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-11.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); //DDB-1785
    }
	
	@Test
    public void create_userMigrationList_xfrlst_postdtFutureDateValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-xfrlst-12.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "xfr", "", "uid", "xfr" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "fauid", "", acct, 1);
		setElementValueInConduitXMLFile(fileName, "xfrlst", "xfr", "tauid", "", acct, 2);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir); //DDB-1786
    }
	
	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException , TransformerFactoryConfigurationError, TransformerException
    {
		deleteAllTestData();
    }

}


