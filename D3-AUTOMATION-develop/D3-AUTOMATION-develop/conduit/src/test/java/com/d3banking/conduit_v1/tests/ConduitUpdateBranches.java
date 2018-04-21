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
public class ConduitUpdateBranches extends ConduitLibrary{
	String fileName1 = "create-branch-test.xml";
	
	@Before
    public void createTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		String path = folderPathWhereDropFile();
		dropConduitTestFile(fileName1, path);
		waitConduitFileProcessing(fileName1, path, 20);
		validateConduitBranch(fileName1,null, false, false, false);
		deleteTrackedFiles(getProcessFolderName());
		deleteTrackedFiles(getErrFolderName ());
		deleteTrackedFiles(getFailedFolderName () );
		deleteTrackedFiles(getPendingFolderName());
    }
	
	@Test
    public void updateBranch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
		//re-drop the file
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_MultipleBranchesWithOneInvalidBranch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-MultipleBranchesWithOneInvalidBranch.xml";
		int _numOfExistBranches= Integer.parseInt(runQuery(connectToDB(), "SELECT COUNT(*) FROM [BRANCH]", false));
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		Thread.sleep(3000);
		validateNumOfRecords(_numOfExistBranches, 0);
		validateSystemEventErrMsg("element 'state' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_MultipleBranchesWithNoneExistBranch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-MultipleBranchesWithNoneExistBranch.xml";
		int _numOfExistBranches= Integer.parseInt(runQuery(connectToDB(), "SELECT COUNT(*) FROM [BRANCH]", false));
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateNumOfRecords(_numOfExistBranches, 1);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_BusinessNameNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-BusinessNamePhoneNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, true);
		validateSystemEventErrMsg("The value '' of element 'name' is not valid");
		validateSystemEventErrMsg("The value '' of element 'phone' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_BusinessPAddressElementToNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-PhysAddressElementsToNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, true);
		validateSystemEventErrMsg("The value '' of element 'address1' is not valid");
		validateSystemEventErrMsg("The value '' of element 'city' is not valid");
		validateSystemEventErrMsg("The value '' of element 'state' is not valid");
		validateSystemEventErrMsg("The value '' of element 'postalCode' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_BusinessPAddressGeoCodeNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-PhysAddressGeoCodeNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
		@Test
    public void updateBranch_BusinessMAddressElementsNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-MailAddressElementsNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, true);
		validateSystemEventErrMsg("The value '' of element 'address1' is not valid");
		validateSystemEventErrMsg("The value '' of element 'city' is not valid");
		validateSystemEventErrMsg("The value '' of element 'state' is not valid");
		validateSystemEventErrMsg("The value '' of element 'postalCode' is not valid");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
		
	@Test
    public void updateBranch_OptionalParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-optionalParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_physicalAddressOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-physicalAddressOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_RequredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-requiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, true);
		validateSystemEventErrMsg("content of element 'contact1' is not complete");
		validateSystemEventErrMsg("content of element 'contact2' is not complete");
		//validateSystemEventErrMsg("The content of element 'business' is not complete");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_physicalAddressElementNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-physicalAddressElementNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateSystemEventErrMsg("The content of element 'physicalAddress' is not complete.");
		validateFileStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
	    validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_mailingAddressElementNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-mailingAddressElementNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_contact1AND2ElementsNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "update-branch-test-contact1AND2ElementsNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateFileNotStoredIn(getFailedFolderName () );
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName ());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_versionElementMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingVersion.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, false);
		validateSystemEventErrMsg("The content of element 'conduitBranchRequest' is not complete. One of");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void updateBranch_authenticationElementNodeMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingAuthenticationElementNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 20);
		validateConduitBranch(fileName1, fileName, false, true, true);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@After
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }
}
