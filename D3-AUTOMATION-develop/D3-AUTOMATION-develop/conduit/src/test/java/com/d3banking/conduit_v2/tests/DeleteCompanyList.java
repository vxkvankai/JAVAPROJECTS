package com.d3banking.conduit_v2.tests;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v2.functions.ConduitLibrary;


public class DeleteCompanyList extends ConduitLibrary{
	String compUid = "comp" + getTodayDate();
	String busnNm = "bn" + getTodayDate();

	@Before
    public void setup() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException, TransformerFactoryConfigurationError, TransformerException 
    {	
		String fileName = "conduit-companyList-18.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		deleteAllTestData();
		deleteConduitFiles();
		String path = folderPathWhereDropFile();
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		deleteConduitFiles();
    }
	
	@Test
    public void delete_compExist_setDeletionFlagToTrueFalse() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-01.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		//validateNoSystemEventCreated(); //DDB-1698
		checkCompanyListDeletionSet(fileName, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		//validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
		deleteConduitFiles();
		
		fileName = "conduit-companyList-remove-02.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		//validateNoSystemEventCreated(); //DDB-1698
		checkCompanyListDeletionSet(fileName, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		//validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nonExistentComp_setDeletionFlagToTrue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-03.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", "comp2" + getTodayDate());
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", "busn2" + getTodayDate());
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkNumOfBusProdComp(fileName, 1, 0, 1);
		checkCompanyListDeletionSet(fileName, false);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_compAccProdExist_setDeletionFlagToTrueFalse() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-04.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkAccProdListDeletionSet(fileName, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
		deleteConduitFiles();
		
		fileName = "conduit-companyList-remove-05.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated();
		checkAccProdListDeletionSet(fileName, true);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nonAccProdExistentComp_setDeletionFlagToTrue() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-06.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkNumOfBusProdComp(fileName, 1, 1, 1);
		checkAccProdListDeletionSet(fileName, false);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
/*	@Test
    public void delete_nullifyBusOptionalElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-07.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkCompElementsNullify(fileName); //DDB-1699 complete this once have fix for it!
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }*/
	
	@Test
    public void delete_nullifyBusRequredElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-08.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("tp");
		validateSystemEventErrMsg("busn");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	
/*	@Test
    public void delete_nullifyPAddressOptionalElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-09.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkCompElementsNullify(fileName); //DDB-1699 complete this once have fix for it!
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }*/
	
	@Test
    public void delete_nullifyPAddressRequredElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-10.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: busn.paddr.st; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: busn.paddr.ct; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: busn.paddr.pc; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	/*@Test
    public void delete_nullifyMAddressOptionalElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-11.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkCompElementsNullify(fileName); //DDB-1699 complete this once have fix for it!
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }*/
	
	@Test
    public void delete_nullifyMAddressRequredElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-12.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: busn.maddr.st; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: busn.maddr.ct; value: ; constraint: may not be empty");
		validateSystemEventErrMsg("property: busn.maddr.pc; value: null; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyContact1OptionalElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-13.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkCompElementsNullify(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyContact1RequredElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-14.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: busn.ct1.ln; value: ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyContact2OptionalElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-15.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkCompElementsNullify(fileName);
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	@Test
    public void delete_nullifyContact2RequredElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-16.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: busn.ct2.ln; value: ; constraint: may not be empty");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
/*	@Test
    public void delete_nullifyAccProductOptionalElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-17.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateNoSystemEventCreated(); 
		checkCompElementsNullify(fileName);
		validateFileStoredIn(proccessedDir); //DDB-1699 complete this once have fix for it!
		validateFileNotStoredIn(failedDir);
		validateFileNotStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }*/
	
	@Test
    public void delete_nullifyAccProductRequredElementValues() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, TransformerFactoryConfigurationError, TransformerException
    {
		String path = folderPathWhereDropFile();
		String fileName = "conduit-companyList-remove-18.xml";
		setElementValueInConduitXMLFile(fileName, "comp", "", "uid", compUid);
		setElementValueInConduitXMLFile(fileName, "busn", "nm", "", busnNm);
		dropConduitTestFile(fileName, path);
		waitConduitFileProcessing(fileName, path, 120);
		validateSystemEventErrMsg("property: ac; value");
		validateFileStoredIn(proccessedDir);
		validateFileNotStoredIn(failedDir);
		validateFileStoredIn(errorDir);
		validateFileNotStoredIn(duplicateDir);
    }
	
	
	@After
    public void after() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, InterruptedException 
    {
		deleteAllTestData();
    }
}
