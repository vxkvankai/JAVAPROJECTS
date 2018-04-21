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
public class ConduitUserMigration extends ConduitLibrary{
	
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
	
/*	@Test
    public void userMigration_userMigration() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertDestList(fileName, true, 1);
		validateAlertList(fileName, true);
		validateMsgList(fileName, true, 1);
		validateCategoryList(fileName, true, 1);
		validateCategorizationMappingList(fileName, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); 
    } */
	
	@Test
    public void userMigration_alertDestList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertDestList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertDestList(fileName, true, 1);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void userMigration_alertDestList_requiredElementsSetToEmptyValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertDestList_requiredElementsNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertDestList(fileName, false, 1);
		validateSystemEventErrMsg("The value '' of element 'externalId' is not valid.");
		validateSystemEventErrMsg("The value '' of element 'name' is not valid.");
		validateSystemEventErrMsg("The value '' of element 'type' is not valid.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
/*	@Test
    public void userMigration_alertDestList_sameExternalIds() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertDestList_sameExternalIDs.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertDestinationList(fileName, false, 1);
		validateSystemEventErrMsg("The value '' of element 'externalId' is not valid.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1454
    } */
	
	@Test
    public void userMigration_alertList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1492, APDB-1505, APDB-1510, APDB-1511, APDB-1512
    } 
	
	@Test
    public void userMigration_alertList_optionalElementsOmitted_accExId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_optionalElementsOmitted_accExId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }

/*	@Test
    public void userMigration_alertList_optionalElementsOmitted_ignoreDnd() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_optionalElementsOmitted_ignoreDnd.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1487
    }*/
	
	@Test
    public void userMigration_alertList_requireElementsOmitted_alertType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_requiredElementsOmitted_alertType.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, false);
		validateSystemEventErrMsg("alertType");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void userMigration_alertList_requireElementsOmitted_alertDestList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_requiredElementsOmitted_alertDestList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, false);
		validateSystemEventErrMsg("externalId}' is expected");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void userMigration_alertList_invalidAlertType() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_invalidAlertType.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, false);
		validateSystemEventErrMsg("of element 'alertType' is not valid.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void userMigration_alertList_invalidDelFrequency() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_invalidDelFrequency.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, false);
		validateSystemEventErrMsg("Value 'invalid.deliveryFrequency' is not");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void userMigration_alertList_elementsSetToNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_elementsSetToNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, false);
		validateSystemEventErrMsg("The value '' of element 'accountDirectId' is not valid");
		validateSystemEventErrMsg("The value '' of element 'deliveryFrequency' is not valid.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1489
    }
	
	@Test
    public void userMigration_alertList_invalidAccExternalId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_invalidAccExternalId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, false);
		validateSystemEventErrMsg("Unable to locate UserAccount for specified userId/accountDirectId");
		validateFileStoredIn(getProcessFolderName());
		//validateFileStoredIn(getFailedFolderName () ); APDB-1514
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }

/*	@Test
    public void userMigration_alertList_invalidDestExternalId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_alertList_invalidDestExternalId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateAlertList(fileName, false);
		validateSystemEventErrMsg("");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1491
    }*/

	@Test
    public void userMigration_messageList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_messageList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateMsgList(fileName, true, 1);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void userMigration_messageList_RequiredElementsSetToEmptyValue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_messageList_RequiredElementsEmptyValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateMsgList(fileName, false, 1);
		validateSystemEventErrMsg("The value '' of element 'msgType' is not valid.");
		validateSystemEventErrMsg("The value '' of element 'subject' is not valid.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void userMigration_messageList_RequiredElementsOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_messageList_requiredElementsOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateMsgList(fileName, false, 1);
		validateSystemEventErrMsg("msgType");
		validateSystemEventErrMsg("subject");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
/*	@Test
    public void userMigration_categoryList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categoryList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategoryList(fileName, true, 1);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1513, APDB-1535
    } */
	
	@Test
    public void userMigration_categoryList_requiredElements1EmptyValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categoryList_requiredElements1EmptyValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategoryList(fileName, false, 1);
		validateSystemEventErrMsg("The value '' of element 'externalId' is not valid.");
		validateSystemEventErrMsg("The value '' of element 'categoryType' is not valid.");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void userMigration_categoryList_requiredElements2EmptyValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categoryList_requiredElements2EmptyValues.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategoryList(fileName, false, 1);
		validateSystemEventErrMsg("categoryLevel");
		validateSystemEventErrMsg("categoryGroup");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
	@Test
    public void userMigration_categoryList_requiredElementsNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categoryList_requiredElementsNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategoryList(fileName, false, 1);
		validateSystemEventErrMsg("externalId");
		validateSystemEventErrMsg("categoryType");
		validateSystemEventErrMsg("categoryLevel}' is expected.");
		validateSystemEventErrMsg("categoryGroup");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 

/*	@Test
    public void userMigration_categoryList_SameExternalIDs() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categoryList_sameExternalIDs.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategoryList(fileName, false, 1);
		validateSystemEventErrMsg("externalId");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }*/  //APDB-1462
	
/*	@Test
    public void userMigration_CategorizationMappingList() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categorizationMappingList.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategorizationMappingList(fileName, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());  //APDB-1497, APDB-1515, APDB-1534
    } */
	
	@Test
    public void userMigration_CategorizationMappingList_requiredElementOmitted_categoryExternalId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categorizationMappingList_requiredElementOmitted_categoryExternalId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategorizationMappingList(fileName, false);
		validateSystemEventErrMsg("categoryExternalId}' is expected");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    } 
	
/*	@Test
    public void userMigration_CategorizationMappingList_requiredElementOmitted_trDirectId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categorizationMappingList_requiredElementOmitted_trDirectId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategorizationMappingList(fileName, false);
		validateSystemEventErrMsg("");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1494
    }*/
	
	/*@Test
    public void userMigration_CategorizationMappingList_invalidCategoryExternalId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categorizationMappingList_invalidCategoryExternalId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategorizationMappingList(fileName, false);
		validateSystemEventErrMsg("");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1495
    }
	
	@Test
    public void userMigration_CategorizationMappingList_invalidTrDirectId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categorizationMappingList_invalidTrDirectId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategorizationMappingList(fileName, false);
		validateSystemEventErrMsg("");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1496
    }
	
	@Test
    public void userMigration_CategorizationMappingList_invalidSplitId() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, ParseException
    {
		String path = folderPathWhereDropFile();
		String fileName = "migration-test_categorizationMappingList_invalidSplitId.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateCategorizationMappingList(fileName, false);
		validateSystemEventErrMsg("");
		validateFileStoredIn(getProcessFolderName());
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName()); //APDB-1496
    }*/
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }
	
}


//scheduledTransferList test when transfer is disabled
//when all bugs fixed, complete userMigration
//if param = value, value = omitted


