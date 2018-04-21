package com.d3banking.conduit_v1.tests;
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v1.functions.ConduitLibrary;


@RunWith(JUnit4.class)
public class ConduitDisassociateUserAccount extends ConduitLibrary{

	String fileName1 = "create-user-test-disassociateUserAccount.xml";

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
    public void disassociateUserAccount_OnlyUserAccountListElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-disassociateUserAccount_OnlyUserAccountListElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, false, true, false,true,false, fileName, false, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void disassociateUserAccount_disassociate1rtUser_associate2ndUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-disassociateUserAccount_disassociate1rtUser_associate2ndUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, false,true,false, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void disassociate1stUserAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-dissassociateUserAccount_2ndUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		path = folderPathWhereDropFile();
		fileName = "update-user-test-disassociateOneUserAccount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
    }
	
	@Test
    public void disassociateBothUserAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-dissassociateUserAccount_2ndUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		path = folderPathWhereDropFile();
		fileName = "update-user-test-disassociateBothUserAccount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, false, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
    }
	
	@Test
    public void disassociateUserAccount_invalidAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-disassociateUserAccount_invalidAccount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("userAccountList");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }

	
}