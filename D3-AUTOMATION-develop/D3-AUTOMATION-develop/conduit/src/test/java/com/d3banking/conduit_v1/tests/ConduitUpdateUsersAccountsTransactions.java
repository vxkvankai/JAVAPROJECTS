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
public class ConduitUpdateUsersAccountsTransactions extends ConduitLibrary{

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
    public void updateUsers_updateUsersAccountsTransactions() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateSyncDate(startSyncDate, getSystemDate()); 
    } 
	
	@Test
    public void updateUsers_updateInternalToExternal() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test_updateInternalToExternal.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateSyncDate(startSyncDate, getSystemDate());
    } 
	
	@Test
    public void updateUsers_updateExternalAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test_updateIExternal.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateSyncDate(startSyncDate, getSystemDate());
    } 
	
	@Test
    public void updateUsers_userOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-userOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_userOptionalParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-userOptionalParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void updateUsers_personOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-personOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, false, false, false,false,false, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_personRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-personRequiredElementAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, false, false);
		validateSystemEventErrMsg("The value '' of element 'firstName' is not valid");
		validateSystemEventErrMsg("The value '' of element 'lastName' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_personRequiredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-personRequiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, false, false);
		validateSystemEventErrMsg("The content of element 'person' is not complete.");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_credentialsOptionalElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-credentialsOptionalElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_credentialsOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-credentialsOptionalElementsAreOmtted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_credentialsRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-requiredElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_credentialsRequiredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-credentialsRequiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void updateUsers_userAccountOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-userAccountOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_userAccountActionAttributeOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-userAccountActionAttributeOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_accountOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-accountOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_accountOptionalParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-accountOptionalParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_localTransactionOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-trOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_transactionRequiredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-trRequiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, false);
		validateSystemEventErrMsg("The content of element 'transaction' is not complete");
		validateSystemEventErrMsg("type");
		validateSystemEventErrMsg("amount");
		validateSystemEventErrMsg("postDate");
		validateSystemEventErrMsg("pending");
		validateSystemEventErrMsg("postingSeq");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void updateUsers_userAccountListElementsWhichDoNotAcceptNullValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-userAccountListElementsWhichDoNotAcceptNullValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, false, false);
		validateSystemEventErrMsg("excluded");
		validateSystemEventErrMsg("property of 'hidden' is false");
		validateSystemEventErrMsg("must not appear on element 'hideByAdmin'");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_accountListElementsWhichDoNotAcceptNullValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-accListElementsWhichDoNotAcceptNullValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, false);
		validateSystemEventErrMsg("The value '' of element 'balance' is not valid");
		validateSystemEventErrMsg("The value '' of element 'accountType' is not valid");
		validateSystemEventErrMsg("The value '' of element 'accountClass' is not valid");
		validateSystemEventErrMsg("he value '' of element 'availableBalance' is not valid.");
		validateSystemEventErrMsg("The value '' of element 'currencyCode' is not valid");
		validateSystemEventErrMsg("The value '' of element 'rateOfReturn' is not valid");
		validateSystemEventErrMsg("The value '' of element 'availableCredit' is not valid");
		//validateSystemEventErrMsg("The value '' of element 'restricted' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_accountListElements2WhichDoNotAcceptNullValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-accListElements2WhichDoNotAcceptNullValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, false);
		//validateSystemEventErrMsg("The value '' of element 'estatements' is not valid");
		validateSystemEventErrMsg("The value '' of element 'accountOpenDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'lastStatementDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'lastDepositAmount' is not valid");
		validateSystemEventErrMsg("The value '' of element 'lastDepositDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'rewardsBalance' is not valid");
		validateSystemEventErrMsg("The value '' of element 'pointsAccrued' is not valid");
		validateSystemEventErrMsg("The value '' of element 'pointsRedeemed' is not valid");
		validateSystemEventErrMsg("The value '' of element 'branchExternalId' is not valid");
		validateSystemEventErrMsg("The value '' of element 'routingTransitNumber' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_transactionElementsWhichDoNotAcceptNullValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-trElementsWhichDoNotAcceptNullValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, false);
		validateSystemEventErrMsg("The value '' of element 'amount' is not valid.");
		validateSystemEventErrMsg("The value '' of element 'postDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'type' is not valid");
		//validateSystemEventErrMsg("The value '' of element 'pending' is not valid");
		validateSystemEventErrMsg("The value '' of element 'mcc' is not valid");
		validateSystemEventErrMsg("The value '' of element 'principalAmount' is not valid");
		validateSystemEventErrMsg("The value '' of element 'interestAmount' is not valid");
		validateSystemEventErrMsg("The value '' of element 'otherAmount' is not valid");
		validateSystemEventErrMsg("The value '' of element 'currencyCode' is not valid");
		validateSystemEventErrMsg("The value '' of element 'runningBalance' is not valid");
		validateSystemEventErrMsg("The value '' of element 'originationDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'postingSeq' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_updateAllElementsWhichDOAcceptNullValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-updateAllElementsWhichDoAcceptNullValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_noneExistingBranchID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-nonExistingBranchID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, false, false); 
		validateSystemEventErrMsg("Invalid branchExternalId");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void updateUsers_missingAuthenticationNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingAuthenticationNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_authenticationElementsNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-authenticationElementsNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateSystemEventErrMsg("The value '' of element 'username' is not valid");
		validateSystemEventErrMsg("The value '' of element 'password' is not valid");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_missingTransactionDateRangeNode_trExists() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingtrDateRange.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, false);
		validateSystemEventErrMsg("Invalid transactionDateRange specified");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_missingTransactionDateRangeNode_RequiredTransactionsNotExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingtrDateRange_trNotExist.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_missingUserListNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingUserListNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, false, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_missingAccountListNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingAccountListNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_missingUserAccountListNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String  fileName = "update-user-test-missingUserAccountListNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_missingCredentialsNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingCredentialsNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_addCredentials() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {		
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingCredentialsNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		fileName = "update-user-test-addCredentials.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
    }
	
    @Test
    public void updateUsers_missingUserOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingUserOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_missingAccountListOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-missingAccountListOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_deleteCredentialLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deleteCredentials.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_deleteMailingAddressLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deleteMailingAddressEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void updateUsers_deletePhysicalAddressLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deletePhysicalAddressByEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void updateUsers_deletePersonLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deletePersonLeavingEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void updateUsers_deleteUserAccountLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deleteUserAccountLeavingEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_deleteUserLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deleteUserLeavingEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_deleteTransactionLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deleteTransactionLeavingEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_deleteAccountLeavingEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-deleteAccountLeavingEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true, true, true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void updateUsers_moreThanOneAccountNickName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test-moreThanOneAccountNickName.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateUsers_removeElementValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test_remoteValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, fileName, false, true);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void updateUsers_updateSplits() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-user-test_splitTr.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, fileName, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateSyncDate(startSyncDate, getSystemDate());   //APDB-1493 (did update second user split amounts)
    } 
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }

	
}
