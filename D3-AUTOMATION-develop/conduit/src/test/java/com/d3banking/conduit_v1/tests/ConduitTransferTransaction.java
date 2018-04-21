package com.d3banking.conduit_v1.tests;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v1.functions.ConduitLibrary;


@RunWith(JUnit4.class)
public class ConduitTransferTransaction extends ConduitLibrary{

	String fileName1 = "create-user-test.xml";

	@Before
    public void updateTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName,null, false, false, false);
		
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		deleteConduitFiles();
    }
	
	@Test
    public void transferTransaction_createFromAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_createFromAccount.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false,true,false, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateSyncDate(startSyncDate, getSystemDate());
    } 
	
	@Test
    public void transferTransaction_updateFromAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 200);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateSyncDate(startSyncDate, getSystemDate());
    } 
	
	@Test
    public void transferTransaction_invalidToAccountDirectID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_invalidToAccountDirectID.xml";
		//String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false); 
		validateSystemEventErrMsg("Invalid toAccountDirectId");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		//validateSyncDate(startSyncDate, getSystemDate()); APDB-795
    } 
	//invalid toAccountName, toAccountRoutingTransferNumber, toAccountNumber, toAccountType
	
	@Test
    public void transferTransaction_requiredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_requiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false); 
		validateSystemEventErrMsg("toAccountRoutingTransitNumber");
		validateSystemEventErrMsg("toAccountNumber");
		validateSystemEventErrMsg("toAccountType");
		validateSystemEventErrMsg("startDate");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void transferTransaction_requiredElementsSetToNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_requiredElementsSetToNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false); 
		validateSystemEventErrMsg("toAccountType");
		validateSystemEventErrMsg("startDate");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void transferTransaction_requiredElementsSetToNull_accNum() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_requiredElementsSetToNull_accNum.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false); 
		validateSystemEventErrMsg("toAccountNumber");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
	@Test
    public void transferTransaction_requiredElementsSetToNull_toAccRoutingTrNum() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_requiredElementsSetToNull_toAccRoutingTrNum.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false); 
		validateSystemEventErrMsg("toAccountRoutingTransitNumber");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void transferTransaction_optionalElementAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_optionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
/*	@Test
    public void transferTransaction_nullifyElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_nullifyElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-811
    }*/
	
	@Test
    public void transferTransaction_invalidFrequency() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "transfer-tr-test_invalidFrequency.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false); 
		validateSystemEventErrMsg("frequency");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
//transfer required - APDB-813
//frequency required - APDB-812
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }

	
}
