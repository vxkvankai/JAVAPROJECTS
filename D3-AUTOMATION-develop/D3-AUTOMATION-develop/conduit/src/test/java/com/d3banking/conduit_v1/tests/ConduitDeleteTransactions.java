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
public class ConduitDeleteTransactions extends ConduitLibrary{ 
	
	@Before
    public void createTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException 
    {
		
		String fileName = "create-branch-test.xml";
		deleteAllTestData();
		
		String path = folderPathWhereDropFile();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName, null, false, false, false);
		deleteConduitFiles();
    }
	
	@Test
    public void deleteTransactions_noUserAssociation_deleteSpecificDateRangeTransactions() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles(); 
		
		String fileName2 = "deleteTransactions-test2.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles(); 
    }
	
	@Test
    public void deleteTransactions_noUserAssociation_deleteAllTransactions() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test3.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles(); 
    }
	
	@Test
    public void deleteTransactions_noUserAssociation_allTransactionsListedIn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test4.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles(); 
    }
	
	@Test
    public void deleteTransactions_onlyUserAssociation_noAccountListInXML() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles(); 
		
		String fileName2 = "deleteTransactions-test5.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles(); 
    }
	
	@Test
    public void deleteTransactions_noDateValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles(); 
		
		String fileName2 = "deleteTransactions-test6.xml";
		runQuery(connectToDB(),"DELETE from [SYSTEM_EVENT]", true);
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateSystemEventErrMsg("The value '' of element 'beginDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'endDate' is not valid");
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
    }
	
	@Test
    public void deleteTransactions_wrongDateFormat() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test7.xml";
		runQuery(connectToDB(),"DELETE from [SYSTEM_EVENT]", true);
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateSystemEventErrMsg("of element 'beginDate' is not valid");
		validateSystemEventErrMsg("of element 'endDate' is not valid");
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles(); 
    }
	
	@Test
    public void deleteTransactions_noUserAssociation_exactDateForDateRange() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test8.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles();  
    }
	
	@Test
    public void deleteTransactions_noUserAssociation_addNewTranWhileDelete() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test9.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 1);
		deleteConduitFiles();  
    }

	@Test
    public void deleteTransactions_noUserAssociation_onlyTrDateRangeNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test10.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
    }
	
/*	@Test
    public void deleteTransactions_noUserAssociation_wrongDateRange() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test11.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, false, false, true, false,true,false, null, false, false);
		validateSystemEventErrMsg("nvalid transaction date range specified at 'transactionDateRange'"); //APDB-1397
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
    }*/
	
	@Test
    public void deleteTransactions_deleteSpecificDateRangeTransactions() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1-1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test2.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles();
    }
	
	@Test
    public void deleteTransactions_deleteAllTransactions() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1-1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test3.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles();
    }
	
	@Test
    public void deleteTransactions_allTransactionsListedIn() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1-1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test4.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles();
    }
	
	@Test
    public void deleteTransactions_onlyAccountListInXML() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1-1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles(); 
		
		String fileName2 = "deleteTransactions-test5.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles(); 
    }
	
	@Test
    public void deleteTransactions_exactDateForDateRange() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1-1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test8.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0);
		deleteConduitFiles();
    }
	
	@Test
    public void deleteTransactions_addNewTranWhileDelete() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1-1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test9.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, true, true, true, true,true,true, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 1);
		deleteConduitFiles(); 
    }
	
	@Test
    public void deleteTransactions_onlyTrDateRangeNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test1-1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
		
		String fileName2 = "deleteTransactions-test10.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
    }
	
	@Test
    public void deleteTransactions_trOutsideDateRange() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "deleteTransactions-test12.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, false,false,false, null, false, false);
		validateSystemEventErrMsg("Incoming transaction has postDate which falls outside provided TransactionDateRange", 45);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		deleteConduitFiles();
    }
	
	
	@Test
    public void arvestFinding1() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName1 = "xArvestFinding1-test-file1.xml";
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		String fileName2 = "xArvestFinding1-test-file2.xml";
		dropConduitTestFile(fileName2, path);
		waitConduitFileProcessing(fileName2, path, 20);
		validateConduitUsersAndAccounts(fileName2, false, false, true, false,true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateTransactionDeletionWithinDateRange(fileName1, fileName2, 0); 
		deleteConduitFiles();
    }
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }
	
}



