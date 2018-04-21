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

public class UpdateUserMigrationList extends ConduitLibrary{
	String tDay = getTodayDate();
	String comp = "comp" + tDay;
	String acct = "acct" + tDay;
	String alertdest = "alertdest" + tDay;
	String txn = "txn" + tDay;
	String cat = "cat" + tDay;
	String usr = "usr" + tDay;
	
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
		
		path = folderPathWhereDropFile();
		fileName = "conduit-userMigrationList-01.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + tDay);
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + tDay);
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
    public void update_allUserMigrationListAllElementsWithSameValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-01.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + tDay);
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + tDay);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "auid", acct);
		setElementValueInConduitXMLFile(fileName, "catmap", "", "tuid", txn);
		setElementValueInConduitXMLFile(fileName, "cattxnlst", "cat", "", "uid", cat, 1);
		setElementValueInConduitXMLFile(fileName, "alert", "alst", "a", "", acct, 1);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("Duplicate File Error");
		validateFileStoredIn(duplicateDir); 
    }
	
	@Test
    public void update_allUserMigrationListAllElementsWithDiffValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		String fileName = "conduit-userMigrationList-52.xml";
		setElementValueInConduitXMLFile(fileName, "usrmig", "", "uid", usr);
		setElementValueInConduitXMLFile(fileName, "acctdisp", "", "uid", acct);
		setElementValueInConduitXMLFile(fileName, "cat", "", "uid", cat);
		setElementValueInConduitXMLFile(fileName, "alertdest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "alert", "", "uid", "alert" + tDay);
		setElementValueInConduitXMLFile(fileName, "destlst", "dest", "", "uid", alertdest);
		setElementValueInConduitXMLFile(fileName, "msg", "", "uid", "msg" + tDay);
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
	
	@After
    public void tearDown() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
		deleteAllTestData();
    }

}

