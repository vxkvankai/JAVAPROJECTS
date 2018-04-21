package com.d3banking.conduit_v1.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.d3banking.conduit_v1.functions.ConduitLibrary;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	ConduitCreateBranches.class,
	ConduitUpdateBranches.class,
	ConduitCreateUsersAccountsTransactions.class,
	ConduitUpdateUsersAccountsTransactions.class,
	ConduitCreateMemoPost.class,
	ConduitUpdateMemoPost.class,
	ConduitDeleteTransactions.class,
	ConduitDisassociateUserAccount.class,
	ConduitUserMigration.class
	//ConduitTransferTransaction.class,
	//ConduitTestsFromAppendix.class
})
public class TestSuiteConduit extends ConduitLibrary{
	
}

