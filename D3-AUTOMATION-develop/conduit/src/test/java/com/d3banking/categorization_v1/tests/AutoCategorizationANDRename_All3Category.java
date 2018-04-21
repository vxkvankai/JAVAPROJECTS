package com.d3banking.categorization_v1.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import com.d3banking.conduit_v1.functions.ConduitLibrary;



@RunWith(JUnit4.class)
public class AutoCategorizationANDRename_All3Category extends ConduitLibrary{
	
	String RULE_NAME              = "AUTO CATEGORIZATION TEST";
	String RULE_DESCRIPTION       = "CATEGORIZATION.*TEST.*";
	String RULE_NEW_DESCRIPTION       = "RENAMING WORKED";
	String RULE_AMOUNT            = "456.55";
	String RULE_TRANSACTION_TYPE  = "0";
	String RULE_ACCOUNT_TYPE      = "accounttype.banking.checking";
	String RULE_DAY_OF_MONTH      = "20";
	String RULE_MCC               = null;
	String _userID				  = "";
	int _categoryID			      = 104;
	String _fileName 			  = "categorization-test-createUser.xml";
	List<String> _userDirectIDs   = new ArrayList<String>();
	
	@Before
    public void createTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {	
		_userID = createUserForCategorizationTest(_fileName);
    }
	
	// THERE ARE MATCHING ONE USER-DEFINED, COMPANY AND SYSTEM CATEGORIZATION RULES
	@Test
    public void autoCategorization_MatchingUserDefinedCategory() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		//CREATE CATEGORIZATION RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
					   "Values ('"+_userID+"', '"+_categoryID+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
					   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'USER', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0') "+
		               insertCategorizationQueryPart() + "Values ('"+_userID+"', '"+(_categoryID+1)+"', 1, '"+RULE_NAME
		               +"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				       "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'COMPANY', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0') "+
					   insertCategorizationQueryPart() + "Values ('"+_userID+"', '"+(_categoryID+2)+"', NULL, '"+RULE_NAME+
					   "', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				       "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')" +
				       insertRenamingQueryPart() + "values ('"+_userID+"',NULL,'"+RULE_NAME+"','"+RULE_DESCRIPTION+"', 'USER "+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'USER', '"
				       +RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')"+
				       insertRenamingQueryPart() + "values (NULL,1,'"+RULE_NAME+"','CATEGORIZATION.*TEST.*', 'COMPANY "+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'COMPANY', '"
				       +RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')"+
				       insertRenamingQueryPart() + "values (NULL,NULL,'"+RULE_NAME+"','"+RULE_DESCRIPTION+"', 'SYSTEM"+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'SYSTEM', '"
				       +RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')";
		runQuery(connectToDB(),_queryCreateRules, true);
		
		//CREATE A TRANSACTION
		String _fileName = "categorization-test6.xml";
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID),_fileName, false, false);
		verifyRenamingRule(_fileName, "USER "+ RULE_NEW_DESCRIPTION);
    }
	
	// THERE ARE MATCHING COMPANY AND SYSTEM CATEGORIZATION RULES
	@Test
    public void autoCategorization_MatchingCompanyCategory() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		//CREATE CATEGORIZATION RULES
		String _queryCreateRules =insertCategorizationQueryPart() + 
				   "Values ('"+_userID+"', '"+(_categoryID+1)+"', 1, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'COMPANY', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0') " + 
				   insertCategorizationQueryPart() +  "Values ('"+_userID+"', '"+(_categoryID+2)+"', NULL, '"+RULE_NAME
				   +"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')"+
				   insertRenamingQueryPart() + "values (NULL,1,'"+RULE_NAME+"','CATEGORIZATION.*TEST.*', 'COMPANY "+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'COMPANY', '"
			       +RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')"+
			       insertRenamingQueryPart() + "values (NULL,NULL,'"+RULE_NAME+"','"+RULE_DESCRIPTION+"', 'SYSTEM "+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'SYSTEM', '"
			       +RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')";
		runQuery(connectToDB(),_queryCreateRules, true);	
		
		//CREATE A TRANSACTION
		String _fileName = "categorization-test7.xml";
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+1),_fileName, false, false);
		verifyRenamingRule(_fileName, "COMPANY "+ RULE_NEW_DESCRIPTION);
    }
	
	// THERE IS MATCHING SYSTEM CATEGORIZATION RULE
	@Test
    public void autoCategorization_MatchingSystemCategory() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		//CREATE CATEGORIZATION RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values ('"+_userID+"', '"+(_categoryID+2)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0') "+
			       insertRenamingQueryPart() + "values (NULL,NULL,'"+RULE_NAME+"','"+RULE_DESCRIPTION+"', 'SYSTEM "+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'SYSTEM', '"
			       +RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')";
		runQuery(connectToDB(),_queryCreateRules, true);
		
		//CREATE A TRANSACTION
		String _fileName = "categorization-test8.xml";
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 60);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+2),_fileName, false, false);
		verifyRenamingRule(_fileName, "SYSTEM "+ RULE_NEW_DESCRIPTION);
    }
	
	// THERE IS NO MATCHING SYSTEM
	@Test
    public void autoCategorization_UncategorizedExpenses() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String _fileName = "categorization-test9.xml";
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (1),_fileName, false, false);
		verifyRenamingRule(_fileName, null);
    }
	
	// THERE IS NO MATCHING SYSTEM
	@Test
    public void autoCategorization_UncategorizedIncome() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		String _fileName = "categorization-test10.xml";
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (100),_fileName, false, false);
		verifyRenamingRule(_fileName, null);
    }
	
	
	@After
    public void cleanUpTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		deleteAllTestData();
    }
}
