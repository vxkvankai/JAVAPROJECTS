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
public class ConduitUpdateMemoPost extends ConduitLibrary{
	String fileName1 = "create-memo-test-accounts.xml";;
	
	@Before
    public void updateTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-branchData.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName,null, false, false, false);
		
		fileName = "create-memo-test-createAccounts.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);	

		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
			
		deleteConduitFiles();
    }
	
	@Test
    public void updateMemo_internalAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-memo-test-internalAccount.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 100);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
		validateSyncDate(startSyncDate, getSystemDate());
    } 
	 
	@Test
    public void updateMemo_externalAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-memo-test-externalAccount.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
		validateSyncDate(startSyncDate, getSystemDate()); 
    } 

	@Test
    public void updateMemo_missingVersionNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-memo-test-missingVersionNode.xml";	
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("version");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void updateMemo_missingAccountOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-memo-test-missingAccountOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void updateMemo_trRequiredElementsAreNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-memo-test-trRequiredElementsAreNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateSystemEventErrMsg("The value '' of element 'name' is not valid");
		validateSystemEventErrMsg("The value '' of element 'type' is not valid");
		validateSystemEventErrMsg("The value '' of element 'amount' is not valid");
		validateSystemEventErrMsg("The value '' of element 'postDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'postingSeq' is not valid");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void updateMemo_missingTrOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-memo-test-missingTrOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void updateMemo_removeElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-memo-test-nullifyElementValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }
	
}
