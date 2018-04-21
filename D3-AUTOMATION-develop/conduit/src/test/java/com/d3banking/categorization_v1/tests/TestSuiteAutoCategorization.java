package com.d3banking.categorization_v1.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.d3banking.conduit_v1.functions.ConduitLibrary;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	AutoCategorizationANDRename_UserDefinedCategory.class,
	AutoCategorizationANDRename_CompanyCategory.class,
	AutoCategorizationANDRename_SystemCategory.class,
	AutoCategorizationANDRename_All3Category.class
})
public class TestSuiteAutoCategorization extends ConduitLibrary{
	
}

