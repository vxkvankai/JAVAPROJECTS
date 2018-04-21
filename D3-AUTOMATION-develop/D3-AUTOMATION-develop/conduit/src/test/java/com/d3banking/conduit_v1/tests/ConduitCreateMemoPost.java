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
public class ConduitCreateMemoPost extends ConduitLibrary{
	String fileName1 = "create-memo-test-createAccounts.xml";
	
	@Before
    public void updateTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-branchData.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName,null, false, false, false);
		
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);	
				
		deleteConduitFiles();
    }
	
	@Test
    public void createMemo() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-accounts.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 200);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		//validateFileNotStoredIn(getErrFolderName ()); //APDB-818
		validateFileNotStoredIn(getPendingFolderName()); 
		validateSyncDate(startSyncDate, getSystemDate());
    }
	
	@Test
    public void createMemo_nonExistingAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-nonExistingAccount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("Non-existent account");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createMemo_missingVersionNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingVersionNode.xml";
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
    public void createMemo_missingAuthenticationNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingAuthenticationNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	 
	@Test
    public void createMemo_missingAccountListNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingAccountListNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createMemo_missingAccountNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingAccountNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("The content of element 'accountList' is not complete.");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createMemo_missingTransactionNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTransactionNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("The content of element 'transactionList' is not complete.");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createMemo_accountRequiredElementsAreNull_localAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-accountRequiredElementsAreNull_localAccount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("The value '' of element 'branchExternalId' is not valid.");
		validateSystemEventErrMsg("The value '' of attribute 'directId' on element 'account");
		validateSystemEventErrMsg("The value '' of element 'routingTransitNumber' is not valid");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
	@Test
    public void createMemo_accountRequiredElementsAreNull_externalAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-accountRequiredElementsAreNull_externalAccount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'interfaceId' on element 'account' is not valid");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }  
	
	@Test
    public void createMemo_missingAccountOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingAccountOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createMemo_missingTrRequiredElements_amount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrRequiredElements_amount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("amount");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createMemo_missingTrRequiredElements_type() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrRequiredElements_type.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("type");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    } 
	
	@Test
    public void createMemo_missingTrRequiredElements_name() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrRequiredElements_name.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("name");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createMemo_missingTrRequiredElements_postDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrRequiredElements_postDate.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("postDate");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createMemo_missingTrRequiredElements_pending() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrRequiredElements_pending.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("pending");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createMemo_missingTrRequiredElements_postingSeq() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrRequiredElements_postingSeq.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("postingSeq");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createMemo_missingTrDirectID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrDirectID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, false,false,false, null, false, false);
		validateSystemEventErrMsg("All transactions must have either directId or interfaceId specified, but not both");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createMemo_trRequiredElementsAreNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-trRequiredElementsAreNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("The value '' of element 'name' is not valid");
		validateSystemEventErrMsg("The value '' of element 'type' is not valid");
		validateSystemEventErrMsg("The value '' of element 'amount' is not valid");
		validateSystemEventErrMsg("The value '' of element 'postDate' is not valid");
		validateSystemEventErrMsg("The value '' of element 'postingSeq' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createMemo_trIntefaceIDNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-trInterfaceIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'interfaceId' on element 'transaction' is not valid with respect to its type");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
	@Test
    public void createMemo_trInterfaceIDDirectIDBothNotNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-trInterfaceIDDirectIDBothNotNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, false,false,false, null, false, false);
		validateSystemEventErrMsg("All transactions must have either directId or interfaceId specified, but not both"); 
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createMemo_missingTrOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createMemo_duplicateError_processSucceed() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-accounts.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		//validateFileNotStoredIn(getErrFolderName ()); //APDB-818
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		//validateFileNotStoredIn(getErrFolderName ()); //APDB-818
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		validateFileStoredIn(getDupFolderName(), 1); 

    }
	
	@Test
    public void createMemo_duplicateError_processFail() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-missingTrRequiredElements_amount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName()); 
		
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName1, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName ());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName(), 1); 
    }
	
	@Test
    public void createMemo_maxErrorThresholdExceed() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-memo-test-maxErrorThresholdExceed.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitUsersAndAccounts(fileName, false, false, false, false,false,false, null, false, false);
		validateSystemEventErrMsg("Error threshold exceeded while processing conduit memo post request.");
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
