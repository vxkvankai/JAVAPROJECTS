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
public class ConduitTestsFromAppendix extends ConduitLibrary{
	
	String path, fileName = null;
	
	@Before
    public void createTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		deleteConduitFiles();
    }
	
	@Test
    public void appendixTests() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		//B.1 – Inserting a new Branch
		
		path = folderPathWhereDropFile();
		fileName = "appendixB1-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 40);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		//B.2 – Updating an Existing Branch
		
		path = folderPathWhereDropFile();
		fileName = "appendixB2-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 40);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		
		// A.1 – Migrating new Users, Accounts, Transactions, and Account Associations
		
		path = folderPathWhereDropFile();
		fileName = "appendixA1-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //removed code incomplete areas, make sure to add when code is available!
		deleteConduitFiles();
		
		// A.2 – Updates to User Data
		
		path = folderPathWhereDropFile();
		fileName = "appendixA2-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();   //APDB-472
		
		// A.3 – Updates to User Account Associations
		
		path = folderPathWhereDropFile();
		fileName = "appendixA3-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		fileName = "appendixA2-test.xml";
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		// A.4 – Updating Account Balances
		
		path = folderPathWhereDropFile();
		fileName = "appendixA4-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		// A.5 – Updating Account Balance and Transactions
		
		path = folderPathWhereDropFile();
		fileName = "appendixA5-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		// Appendix C - Conduit Memo Post File Examples
		
		path = folderPathWhereDropFile();
		fileName = "appendixC1-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		//validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false); //APDB-473
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
    } 

	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		deleteConduitFiles();
    }
	
}
