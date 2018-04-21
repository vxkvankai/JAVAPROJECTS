package com.d3banking.conduit_v1.tests;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v1.functions.ConduitLibrary;


@RunWith(JUnit4.class)
public class ConduitCreateUsersAccountsTransactions extends ConduitLibrary{
	
	@Before
    public void createTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		deleteConduitFiles();
		runQuery(connectToDB(), "DELETE FROM [CAMEL_MESSAGEPROCESSED]", true);
    }
	
	@Test
    public void createUsers_createUsersAccountsTransactions() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test.xml";
		String startSyncDate = getSystemDate();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateSplitTransaction(fileName, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); // APDB-131
		validateSyncDate(startSyncDate, getSystemDate());
    } 
	
	@Test
    public void createUsers_createUsersExternalAccountsTransactions_directIDNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-createUsersExternalAccountsTr_directIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false,false,false, null, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'directId' on element 'account' is not valid with respect to its type");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_userDirectIDNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userDirectIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'directId' on element 'user' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_userDirectID2Null() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userDirectID2Null.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_userOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_userOptionalParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userOptionalParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false,false,false, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_userRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userRequiredElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The value '' of element 'loginId' is not valid");
		validateSystemEventErrMsg("The value '' of element 'taxIdType' is not valid");
		validateSystemEventErrMsg("The value '' of element 'branchExternalId' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_userRequiredElementsAreOmitted_branchExternalID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userRequiredElementsAreOmitted_branchExternalID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("branchExternalId");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_userRequiredElementsAreOmitted_loginID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userRequiredElementsAreOmitted_loginID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("All users must have a (unique) loginId specified");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_userRequiredElementsAreOmitted_taxIDType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userRequiredElementsAreOmitted_taxIDType.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The taxIdType must be specified if taxId is set");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_userRequiredParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userRequiredParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid firstName/lastName");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_personOptionalElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-personOptionalElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_personOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-personOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_personOptionalParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-personOptionalParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	
	@Test
    public void createUsers_personRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-personRequiredElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The value '' of element 'firstName' is not valid");
		validateSystemEventErrMsg("The value '' of element 'lastName' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_personRequiredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-personRequiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The content of element 'person' is not complete.");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_credentialsOptionalElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-credentialsOptionalElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_credentialsOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-credentialsOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }  
	
	@Test
    public void createUsers_credentialsRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-credentialsRequiredElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_credentialsRequiredElementsAreOmitted_password() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-credentialRequiredElementsAreOmitted_password.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_credentialsRequiredElementsAreOmitted_profileName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-credentialRequiredElementsAreOmitted_profileName.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_credentialsRequiredElementsAreOmitted_secretQuestion1() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-credentialRequiredElementsAreOmitted_que1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }  
	
	@Test
    public void createUsers_credentialsRequiredElementsAreOmitted_secretAnswer1() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-credentialRequiredElementsAreOmitted_answer1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 

	@Test
    public void createUsers_personPhysicalAddress234Null() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-personPhysAddress23Null.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_userAccountOptionalElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userAccountOptionalElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }  
	
	@Test
    public void createUsers_userAccountOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userAccountOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }  
	
	@Test
    public void createUsers_userAccountRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userAccountRequiredElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Non-existing account");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }     
	
	@Test
    public void createUsers_userAccountActionAttributeOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userAccountActionAttributeOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_userAccountRequiredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userAccountRequiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("Non-existing account");//APDB-350  
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }  
	
/*	@Test
    public void createUsers_accountOptionalElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountOptionalElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-161
    }*/
	
	@Test
    public void createUsers_accountOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_externalAccountOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-externalAccountOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false,true,false, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_externalAccountRequiredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-externalAccountRequiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false,false,false, null, false, false); 
		validateSystemEventErrMsg("New external accounts must have an externalInstitutionId specified");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }  
	
	@Test
    public void createUsers_accountRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The value '' of element 'branchExternalId' is not valid");
		validateSystemEventErrMsg("The value '' of element 'accountName' is not valid");
		validateSystemEventErrMsg("The value '' of element 'accountNumber' is not valid");
		validateSystemEventErrMsg("The value '' of element 'accountType' is not valid");
		validateSystemEventErrMsg("The value '' of element 'accountClass' is not valid");
		validateSystemEventErrMsg("The value '' of element 'routingTransitNumber' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }    
	
/*	@Test
    public void createUsers_accountRequiredElementsAreEmpty_accProdExternalID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreEmpty_accProdExternalID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);//APDB-1451
		validateSystemEventErrMsg("accountProductExternalId");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } */
	
	@Test
    public void createUsers_accountRequiredElementsAreOmitted_accountClass() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreOmitted_accClass.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid accountClass specified for account");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_accountRequiredElementsAreOmitted_branchID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreOmitted_branchExternalID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid external branch id specified for account");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
/*	@Test
    public void createUsers_accountRequiredElementsAreOmitted_accProdExteralID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreOmitted_accProdExteralID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false); //APDB-1451
		validateSystemEventErrMsg("New accounts must have an accountProductExternalId specified");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }*/
	
	@Test
    public void createUsers_accountRequiredElementsAreOmitted_accName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreOmitted_accName.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("New accounts must have an accountName specified");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_accountRequiredElementsAreOmitted_accNum() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreOmitted_accNum.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("New accounts must have an accountNumber specified");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_accountRequiredElementsAreOmitted_accType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreOmitted_accType.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("accountType");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_accountRequiredElementsAreOmitted_routingTransitNumber() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountRequiredElementsAreOmitted_routingTransitNumber.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid routingTransitNumber"); 
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_accountOptionalParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountOptionalParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, false,false,false, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_transactionOptionalElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trOptionalElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_localTransactionOptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trOptionalElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_externalTransactionOptionalElementsAreOmitted_withDirectIDNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trOptionalElementsAreOmitted_externalWithDirectIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false,false,false, null, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'directId' on element 'transaction' is not valid with respect to its type");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_externalTransactionOptionalElementsAreOmitted_withoutDirectID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trOptionalElementsAreOmitted_externalWithoutDirectID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_transactionRequiredElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
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
    public void createUsers_transactionRequiredElementsAreEmpty_pending() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreEmpty_pending.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("pending");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_transactionRequiredElementsAreOmitted_name() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreOmitted_name.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("name");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_transactionRequiredElementsAreOmitted_type() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreOmitted_type.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("type");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_transactionRequiredElementsAreOmitted_amount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreOmitted_amount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("amount");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_transactionRequiredElementsAreOmitted_postDate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreOmitted_postDate.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("postDate");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_transactionRequiredElementsAreOmitted_pending() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreOmitted_pending.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("pending");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_transactionRequiredElementsAreOmitted_postingSeq() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trRequiredElementsAreOmitted_postingSeq.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("postingSeq");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_moreThanOneAccountNickName() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-moreThanOneAccountNickName.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_transactionDirectIDNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trDirectIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'directId' on element 'transaction'");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_trueFalseElementsWithIncorrectValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-trueFalseElementsWithIncorrectValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("of element 'locked' is not valid.");
		validateSystemEventErrMsg("of element 'employee' is not valid.");
		validateSystemEventErrMsg("of element 'excluded' is not valid.");
		validateSystemEventErrMsg("of element 'hidden' is not valid.");
		validateSystemEventErrMsg("of element 'hideByAdmin' is not valid.");
		validateSystemEventErrMsg("is not a valid value for 'boolean'");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    } 
	
	@Test
    public void createUsers_moreThanOneSameLoginIDs() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-moreThan1SameLoginID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("declared for identity constraint \"userLoginIdUniqueConstraint\" of element \"conduitUserRequest\"");
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_wrongFormatDateOfBirth() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-wrongFormatDateOfBirth.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 200);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("of element 'dateOfBirth' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_noneExistingBranchID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-noneExistBranchID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid external branch id specified for account");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_sameAccountDirectIDs() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-sameAccountDirectID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_sameTransactionDirectIDs() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-sameTransactionDirectID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("transactionDirectIdUniqueConstraint");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_noneExistingAccountDirectID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-noneExistAccountDirectID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	
	@Test
    public void createUsers_unknownAccountClass() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-unknownAccountClass.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("of element 'accountClass' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_moreThanOnePersonNodeInOneUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-invalidPerson.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("Invalid content was found starting with element 'person'");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_unknownAccountType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-unknownAccountType.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("of element 'accountType' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_duplicateError_processSucceed() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		verifyDupError();
		
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		verifyDupError();
		validateFileStoredIn(getDupFolderName(), 1); 

    }
	
	@Test
    public void createUsers_duplicateError_processFail() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-userDirectIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		verifyDupError();
		
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		verifyDupError();	
		validateFileStoredIn(getDupFolderName(), 1); 
    } 
	
	@Test
    public void createUsers_duplicateError_updateFailedToCorrectData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-duplicateError.xml";
		setElementValueInConduitXMLFile(fileName, "user", "", "directId", "");
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		verifyDupError();
		
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		verifyDupError();
		
		setElementValueInConduitXMLFile(fileName, "user", "", "directId", "DuplicateError");
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false); 
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());

		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName(), 2);  
    }
	
	
	@Test
    public void createUsers_sameProfileNamesForMoreThanOneUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-sameProfileNames.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("userCredentialsProfileNameUniqueConstraint");
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_profileNameWithSpace() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-profileNameWithSpace.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("profileName cannot contain whitespace characters");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
	@Test
    public void createUsers_passwordWithSpace() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-passwordWithSpace.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("password cannot contain whitespace characters");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
	@Test
    public void createUsers_missingAuthenticationNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingAuthentication.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
	@Test
    public void createUsers_authenticationElementsNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-authenticationNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The value '' of element 'username' is not valid");
		validateSystemEventErrMsg("The value '' of element 'password' is not valid");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTransactionDateRangeNode_RequiredTransactionsExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingtrDateRange_trExists.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false); //did not create user, JIRA
		validateSystemEventErrMsg("The transactionDateRange is required if transactions are presented in the request");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_missingTransactionDateRangeNode_RequiredTransactionsNotExist() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingtrDateRange_trNOTExists.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }

	@Test
    public void createUsers_missingUserListNode_userDoesExistBefore() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingAccountList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		fileName = "create-user-test-missingUserList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, true, false, true, false, null, false, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		fileName = "create-user-test-missingAccountList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		//validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false); //604
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_missingUserListNode_userDoesNotExistBeforeCreateAfterwards() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingUserList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		fileName = "create-user-test-missingAccountList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_missingAccountListNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingAccountList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingUserAccountListNode_AssociationsExistBefore() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String  fileName = "create-user-test-missingAccountList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();

		fileName = "create-user-test-missingUserAccList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	
	@Test
    public void createUsers_missingUserAccountListNode_createAssociationsAfterwards() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingUserAccList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();
		
		fileName = "create-user-test-missingAccountList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false); //fix
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingCredentialsNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingCredentials.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingBranchExternalIDElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingBranchExternalID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("branchExternalId");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingUserOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingOptionalUserElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_missingTaxIDTaxIDTypeElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTaxIDTaxIDTypeElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTaxIDTypeElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTaxIdTypeElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("Invalid taxIdType specified for user");		
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingCredentialOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingCredentialOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false); 
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingCredentialsProfileNameElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingProfileNameElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		deleteConduitFiles();  
    }
	
	@Test
    public void createUsers_missingCredentialsPasswordElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingPasswordElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingCredentialsSecretQuestion1Element() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingSecretQuestionElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingCredentialsSecretAnswer1Element() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingSecretAnswerElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The credentials must have a profileName, password, secretQuestion1, and secretAnswer1");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingUserAccountListOptionalElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingUserAccountListOptionalElements.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingUserAccountListAssociate() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingUserAccountListAssociate.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'action' on element 'userAccount' is not valid");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_missingAccountListOptionalElements_WithUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingAccountListOptionalElements_WithUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_missingAccountListOptionalElements_WithoutUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingAccountListOptionalElements_WithoutUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
	@Test
    public void createUsers_missingAccountListDirectID_NoInterfaceID() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingAccountListDirectID_NoInterfaceID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid directId/interfaceId"); 
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_accountListDirectID_InterfaceIDExists() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-accountListDirectID_InterfaceIDExists.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("All accounts must have either directId or interfaceId specified, but not both");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	
	@Test
    public void createUsers_missingTransactionListNode_WithUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrListNode_WishUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_missingTransactionListNode_NoUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrListNode_NoUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTransactionOptionalElements_WithoutUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrOptionalElements_WithoutUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, true, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_missingTransactionOptionalElements_WittUser() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrOptionalElements_WithUser.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true, true, true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    }
	
	@Test
    public void createUsers_missingTransactionDirectIDANDInterfaceIDElements() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrDirectIdANDInterfaceId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid directId/interfaceId");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  
    }
	
	@Test
    public void createUsers_missingTransactionNameElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrNameElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The content of element 'transaction' is not complete. One of");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTransactionTypeElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrTypeElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The content of element 'transaction' is not complete. One of");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTransactionAmountElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrAmountElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The content of element 'transaction' is not complete. One of");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTransactionPostDateElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrPostDateElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The content of element 'transaction' is not complete. One of");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTransactionPendingElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrPendingElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The content of element 'transaction' is not complete. One of");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingTransactionPostingSeqElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingTrPostingSeqElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("The content of element 'transaction' is not complete. One of");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_validateAllAccountTypesANDAccountClasses() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-allAccountTypesANDClasses.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, true, false, false, false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 

    }
	
	@Test
    public void createUsers_missingVersionElement() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingVersionElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("version");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingUserList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingUserListNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("Invalid content was found starting with element 'user'. One of");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createUsers_missingAccountList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-missingAccountList1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false, false, null, false, false);
		validateSystemEventErrMsg("accountList}' is expected.");
		validateFileNotStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }

	@Test
    public void createUsers_validateTrimsAllTrailingLeadingWhiteSpaces() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-spacesTrim.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, true, true, true,true,true, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_disassociateUserAccountWhichIsNotAssociated() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-disassociateUserAccountWhichIsNotAssociated.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, true, false, true,false, null, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } 
	
/*	@Test
    public void createUsers_maxErrorThresholdExceed() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-maxErrorThresholdExceed.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false,false, null, false, false);	
		validateSystemEventErrMsg("Error threshold exceeded while processing conduit user request");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName()); 
		validateFileStoredIn(getErrFolderName()); 
		validateFileNotStoredIn(getPendingFolderName()); //APDB-482
    } 
	*/
/*	@Test
    public void createUsers_maxErrorThresholdExceed2() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test-maxErrorThresholdExceed2.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, false, false, false, false, false,false, null, false, false);	
		validateSystemEventErrMsg("Error threshold exceeded while processing conduit user request");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName()); 
		validateFileStoredIn(getErrFolderName()); 
		validateFileNotStoredIn(getPendingFolderName()); //APDB-482
    } */
	
	@Test
    public void createUsers_splitNegativeAmount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test_split_negativeAmount.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false,false,false, null, false, false);
		validateSplitTransaction(fileName, false);
		validateSystemEventErrMsg("The value '-14.77' of element 'amount' is not valid.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void createUsers_splitDirectIdOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test_split_directIdOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false,false,false, null, false, false);
		validateSplitTransaction(fileName, false);
		validateSystemEventErrMsg("");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1537
    } 
	
	@Test
    public void createUsers_splitAmountOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-user-test_split_amountOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitUsersAndAccounts(fileName, true, false, false, false,false,false, null, false, false);
		validateSplitTransaction(fileName, false);
		validateSystemEventErrMsg("amount}' is expected.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }
	
}
