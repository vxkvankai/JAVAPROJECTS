package com.d3banking.conduit_v1.tests;
import java.io.IOException;
import java.sql.SQLException;
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
public class ConduitCreateBranches extends ConduitLibrary{
	
	@Before
    public void cleanUpExistTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
		deleteConduitFiles();
    }
	
	@Test
    public void createBranch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 40);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_ExternalIDNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-externalIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'externalId' on element 'branch' is not valid with respect to its type");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessNameNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-BusinessNameNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'name' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessPhoneNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-BusinessPhoneNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'phone' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		
    }
	
	@Test
    public void createBranch_BusinessPAddress1Null() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddress1Null.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'address1' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessPAddressCityNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressCityNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'city' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessPAddressStateNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressStateNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'state' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessPAddressCountryNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressCountryNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessPAddressZipCodeNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressZipCodeNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'postalCode' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessPAddressGeoCodeNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressGeoCodeNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		
    }
	
		@Test
    public void createBranch_BusinessMAddress1Null() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddress1Null.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'address1' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessMAddressCityNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressCityNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'city' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessMAddressStateNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressStateNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'state' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessMAddressCountryNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressCountryNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_BusinessMAddressZipCodeNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-PhysAddressZipCodeNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'postalCode' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_OptionalElementsAreEmpty() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-optionalElementAreEmpty.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_OptionalElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-optionalElementAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_OptionalParentElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-optionalParentElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_physicalAddressOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-physicalAddressOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("Invalid physicalAddress specified for branch");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_RequredElementsAreOmitted() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-requiredElementsAreOmitted.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("content of element 'contact1' is not complete");
		validateSystemEventErrMsg("he content of element 'contact2' is not complete");
		//validateSystemEventErrMsg("The content of element 'business' is not complete");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_MultipleBranchesWithOneInvalidBranch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-MultipleBranchesWithOneInvalidBranch.xml";
		int _numOfExistBranches= Integer.parseInt(runQuery(connectToDB(), "SELECT COUNT(*) FROM [BRANCH]", false));
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateNumOfRecords(_numOfExistBranches, 4);
		validateSystemEventErrMsg("The value '' of element 'name' is not valid");
		validateFileStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_elementMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-MissingElement.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The content of element 'physicalAddress' is not complete");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_duplicateError_processSucceed() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		verifyDupError();
    }
	
	@Test
    public void createBranch_duplicateError_processFail() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-externalIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		verifyDupError();
    }
	
	@Test
    public void createBranch_duplicateError_updateFailedToCorrectData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		//file with externalID null
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-duplicateError.xml";
		setElementValueInConduitXMLFile(fileName, "branch", "", "externalId", "");
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileNotStoredIn(getDupFolderName());
		verifyDupError();
		
		//re-drop the file
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		
		//file with externalID updated
		setElementValueInConduitXMLFile(fileName, "branch", "", "externalId", "DuperTest");
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		Thread.sleep(2000);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
		validateFileStoredIn(getDupFolderName());
		
		//re-drop the file for conduit processing
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getDupFolderName(), 2);

    }

	@Test
    public void createBranch_versionNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-versionNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of element 'version' is not valid");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_versionElementMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingVersion.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The content of element 'conduitBranchRequest' is not complete. One of");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_authenticationElementNodeMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingAuthenticationElementNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, false, false, false);
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_branchListElementNodeMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingBranchListNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The content of element 'conduitBranchRequest' is not complete.");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getProcessFolderName());
		validateFileStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_parentExternalIDMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingParentExternalID.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("Attribute 'parentExternalId' must appear on element 'branch'.");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_parentExternalIDNull() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-parentExternalIDNull.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("The value '' of attribute 'parentExternalId' on element 'branch' is not valid with respect to its type");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
	@Test
    public void createBranch_businessElementNodeMissing() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingBusinessElementNode.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("Invalid physicalAddress specified for branch/business");
		validateFileStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName());
		validateFileNotStoredIn(getErrFolderName());
		validateFileNotStoredIn(getPendingFolderName());
    }
	
/*	@Test
    public void createBranch_missingBranchListNode() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-missingBranchListNode1.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);
		validateSystemEventErrMsg("branchList}' is expected");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileNotStoredIn(getProcessFolderName()); 
		validateFileStoredIn(getErrFolderName());
		//validateFileNotStoredIn(getPendingFolderName()); // don't delete the file here
    }*/ 
	
	@Test
    public void createBranch_validateTrimsAllTrailingLeadingWhiteSpaces() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException
    {
    } 
	
	@Test
    public void createBranch_maxErrorThresholdExceed() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "create-branch-test-maxErrorThresholdExceed.xml";
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 60);
		validateConduitBranch(fileName,null, true, false, false);	
		validateSystemEventErrMsg("ConduitNonCriticalErrorThresholdExceededException: Error threshold exceeded while processing conduit branch request");
		validateFileNotStoredIn(getFailedFolderName());
		validateFileStoredIn(getProcessFolderName()); 
		validateFileNotStoredIn(getErrFolderName());
		//validateFileNotStoredIn(getPendingFolderName()); - start
    }
	
	@After
    public void cleanUpCreatedData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException 
    {
		deleteAllTestData();
    }

}


//PERF TEST: APDB-1049, APDB-1055
