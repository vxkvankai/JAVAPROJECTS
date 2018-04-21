package com.d3.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzerImpl implements IRetryAnalyzer {

    private int retryCount = 0;
    private int maxRetryCount = 1;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public RetryAnalyzerImpl() {
        try {
            maxRetryCount = Integer.parseInt(System.getProperty("retryCount"));
        } catch (Exception e) {
            logger.error("Issue getting the retrCount property", e);
        }
    }

    /**
     * Retry's the test for a configurable amount of times Use: Add 'retryAnalyzer = RetryAnalyzerImpl.class' to the @test decorator Property is
     * retryCount, default is 1 extra retry
     *
     * @param result test result
     * @return True if needs to run again, false otherwise
     */
    public boolean retry(ITestResult result) {
        return false;
    }
}
