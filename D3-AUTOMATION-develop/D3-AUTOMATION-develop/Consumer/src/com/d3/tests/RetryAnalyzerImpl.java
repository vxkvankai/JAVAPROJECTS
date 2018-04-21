package com.d3.tests;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzerImpl implements IRetryAnalyzer {

    public RetryAnalyzerImpl() {
        try {
            int maxRetryCount = Integer.parseInt(System.getProperty("retryCount"));
        } catch (Exception e) {
            log.error("Issue getting the retryCount property", e);
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
