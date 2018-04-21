package com.d3banking.categorization_v1.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
public class AutoCategorizationANDRename_SystemCategory extends ConduitLibrary{
	
	String RULE_NAME              = "SYSTEM AUTO RULE TEST";
	String RULE_DESCRIPTION       = "CATEGORIZATION.*TEST.*";
	String RULE_NEW_DESCRIPTION       = "RENAMING WORKED";
	String RULE_AMOUNT            = "456.55";
	String RULE_TRANSACTION_TYPE  = "0";
	String RULE_ACCOUNT_TYPE      = "accounttype.banking.checking";
	String RULE_DAY_OF_MONTH      = "20";
	String RULE_MCC               = "456";
	String _userID				  = "";
	int _categoryID			      = 104;
	String _fileName 			  = "categorization-test-createUser.xml";
	List<String> _userDirectIDs   = new ArrayList<String>();
	
	
	@Before
    public void createTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {		
		deleteAllTestData();
		_userID = createUserForCategorizationTest(_fileName);
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
					   "Values (NULL, '"+_categoryID+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
					   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')  " + insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+1)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')  \r\n" + 
				   	insertRenamingQueryPart() + "values (NULL,NULL,'"+RULE_NAME+"','"+RULE_DESCRIPTION+"', '"+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'SYSTEM', '"
				   +RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')";
		runQuery(connectToDB(),_queryCreateRules, true);
    }
	
	
	@Test
    public void systemCategorization_OneAllMatch_OthersAmountNotMatch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		_fileName = "categorization-test1.xml";
		List<String> _amount = getElementValueInConduitXMLFile(_fileName, "transaction", "amount", "");
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+3)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+_amount.get(0)+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0') " + 
				   insertRenamingQueryPart() + "values (NULL,NULL,'"+RULE_NAME+"','"+RULE_DESCRIPTION
				   +"', '"+RULE_NEW_DESCRIPTION+"',0,0,0,SYSDATETIME(),'SYSTEM', '"+RULE_AMOUNT+"',"+RULE_MCC+", '"+RULE_DAY_OF_MONTH+"')";
		runQuery(connectToDB(),_queryCreateRules, true);
		
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+3),_fileName, false, false); 
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	@Test
    public void systemCategorization_OneAllMatch_OtherstTypeNotMatch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		_fileName = "categorization-test2.xml";
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+4)+"', NULL, '"+RULE_NAME+"', '100', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')";
		runQuery(connectToDB(),_queryCreateRules, true);
		
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+4),_fileName, false, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	
	@Test
    public void systemCategorization_OneAllMatch_OthersACCTypeNotMatch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		_fileName = "categorization-test3.xml";
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+5)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', 'accounttype.any', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')";
		runQuery(connectToDB(),_queryCreateRules, true);
		
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+5),_fileName, false, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	
	@Test
    public void systemCategorization_OneAllMatch_OthersDAYOFMONTHYNotMatch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
    {
		_fileName = "categorization-test4.xml";
		List<String> _dayOfMonth = getElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "");
		String[] _str = _dayOfMonth.get(0).split("-");
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+6)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+_str[_str.length-1]+"', '0')";
		runQuery(connectToDB(),_queryCreateRules, true);
		
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+6),_fileName, false, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	@Test
    public void systemCategorization_AmountWithNegative() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
    {
		_fileName = "categorization-test11.xml";
		List<String> _amount = getElementValueInConduitXMLFile(_fileName, "transaction", "amount", "");
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+8)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+_amount.get(0)+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')";
		runQuery(connectToDB(),_queryCreateRules, true);
				
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+8),_fileName, false, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	@Test
    public void systemCategorization_MultipleTransactions() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
    {
		_fileName = "categorization-test13.xml";
		List<String> _dayOfMonth = getElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "");
		String[] _str = _dayOfMonth.get(0).split("-");
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+6)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+_str[_str.length-1]+"', '0')";
		runQuery(connectToDB(),_queryCreateRules, true);
				
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+8),_fileName, true, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	@Test
    public void systemCategorization_MultipleTransactionsUsers() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
    {		
		_fileName = "categorization-test14.xml";
		List<String> _dayOfMonth = getElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "");
		List<String> _loginIDs = getElementValueInConduitXMLFile(_fileName, "user", "loginId", "");
		List<String> _userIDs = new ArrayList<String>();
		for (int i = 0; i< _loginIDs.size(); i++)
		{
			_userIDs = runQuery2(connectToDB(), "SELECT  [USER_ID] FROM [OLB_USER] where LOGIN_ID = '" + _loginIDs.get(i)+ "'", false);
		}	
		String[] _str = _dayOfMonth.get(0).split("-");
		
		//CREATE RULES
		for (int i = 0; i< _userIDs.size(); i++)
		{
			String _queryCreateRules = insertCategorizationQueryPart() + 
					   "Values ('"+_userIDs.get(i)+"', '"+(_categoryID+6)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
					   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+_str[_str.length-1]+"', '0')";
			runQuery(connectToDB(),_queryCreateRules, true);
		}
			
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+8),_fileName, true, false); 
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);  //OLB-435
    }
	
	
	@Test
    public void systemCategorization_alreadyCategorizedTrans() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
    {
		_fileName = "categorization-test13.xml";
		List<String> _dayOfMonth = getElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "");
		String[] _str = _dayOfMonth.get(0).split("-");
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+6)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+_str[_str.length-1]+"', '0')";
		runQuery(connectToDB(),_queryCreateRules, true);
				
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+8),_fileName, true, false);
		
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+8),_fileName, true, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	@Test
    public void systemCategorization_addNewAccountWithTrans() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
    {
		_fileName = "categorization-test15.xml";
		List<String> _dayOfMonth = getElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "");
		String[] _str = _dayOfMonth.get(0).split("-");
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values (NULL, '"+(_categoryID+6)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+_str[_str.length-1]+"', '0')";
		runQuery(connectToDB(),_queryCreateRules, true);
				
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+8),_fileName, true, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION);
    }
	
	@Test
    public void systemCategorization_twoUsersSharedAccount() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException
    {
		_fileName = "categorization-test16.xml";
		List<String> _dayOfMonth = getElementValueInConduitXMLFile(_fileName, "transaction", "postDate", "");
		List<String> _loginIDs = getElementValueInConduitXMLFile(_fileName, "user", "loginId", "");
		List<String> _userIDs = new ArrayList<String>();
		for (int i = 0; i< _loginIDs.size(); i++)
		{
			_userIDs = runQuery2(connectToDB(), "SELECT  [USER_ID] FROM [OLB_USER] where LOGIN_ID = '" + _loginIDs.get(i)+ "'", false);
		}	
		String[] _str = _dayOfMonth.get(0).split("-");
		
		//CREATE RULES
		for (int i = 0; i< _userIDs.size(); i++)
		{
			String _queryCreateRules = insertCategorizationQueryPart() + 
					   "Values ('"+_userIDs.get(i)+"', '"+(_categoryID+6)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+RULE_MCC+", " + 
					   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+_str[_str.length-1]+"', '0')";
			runQuery(connectToDB(),_queryCreateRules, true);
		}
				
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 20);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+8),_fileName, true, true);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION); //OLB-435
    }
	
	@Test
    public void systemCategorization_OneAllMatch_OthersMCCNotMatch() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		_fileName = "categorization-test17.xml";
		List<String> _mcc = getElementValueInConduitXMLFile(_fileName, "transaction", "mcc", "");
		
		//CREATE RULES
		String _queryCreateRules = insertCategorizationQueryPart() + 
				   "Values ('"+_userID+"', '"+(_categoryID+6)+"', NULL, '"+RULE_NAME+"', '"+RULE_TRANSACTION_TYPE+"', '"+RULE_ACCOUNT_TYPE+"', '"+RULE_DESCRIPTION+"', "+_mcc.get(0)+" , " + 
				   "'"+RULE_AMOUNT+"', '1', '0', '0', '0', 'SYSTEM', GETDATE(), '"+RULE_DAY_OF_MONTH+"', '0')"+
						   insertRenamingQueryPart() + "values ("+_userID+",NULL,'"+RULE_NAME+"','"+RULE_DESCRIPTION+"', '"+RULE_NEW_DESCRIPTION+ 2+ "',0,0,0,SYSDATETIME(),'COMPANY', '"
						   +RULE_AMOUNT+"',"+_mcc.get(0)+", '"+RULE_DAY_OF_MONTH+"')";
		runQuery(connectToDB(),_queryCreateRules, true);
		
		//CREATE A TRANSACTION
		String _path = folderPathWhereDropFile();
		dropConduitTestFile(_fileName, _path);
		waitConduitFileProcessing(_fileName, _path, 60);
		validateConduitUsersAndAccounts(_fileName, true, true, true, true,true,true, null, false, false);
		verifyCategoryID(_userID, (_categoryID+6),_fileName, true, false);
		verifyRenamingRule(_fileName, RULE_NEW_DESCRIPTION+2);
    }
	

	
	@After
    public void cleanUpTestData() throws ClassNotFoundException, SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
		deleteAllTestData();
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
