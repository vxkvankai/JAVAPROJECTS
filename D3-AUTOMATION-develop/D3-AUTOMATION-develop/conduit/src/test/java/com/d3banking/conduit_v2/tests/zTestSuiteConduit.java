package com.d3banking.conduit_v2.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.d3banking.conduit_v1.functions.ConduitLibrary;



// Conduit Version: Conduit Specification v2.0.14 20140609
// Test scripts coverage: Conduit Specification v2.0.14 20140609

@RunWith(Suite.class)
@Suite.SuiteClasses({
	InsertCompanyList.class,
	UpdateCompanyList.class,
	InsertAccountList.class,
	UpdateAccountList.class,
    InsertUserList.class,
	UpdateUserList.class,
	InsertAllLists.class,   
	UpdateAllLists.class,
	InsertUserMigrationList.class,
	UpdateUserMigrationList.class, //DDB-960
	DeleteAccountList.class,
	DeleteCompanyList.class,
	DeleteUserList.class
})
public class zTestSuiteConduit extends ConduitLibrary{
	
}

