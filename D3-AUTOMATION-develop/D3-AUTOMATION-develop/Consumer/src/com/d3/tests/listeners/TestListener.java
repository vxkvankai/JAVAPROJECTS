package com.d3.tests.listeners;

import com.d3.helpers.TestLogHelper;
import com.d3.tests.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

@Slf4j
public class TestListener implements ITestListener, IInvokedMethodListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Do nothing for right now
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Do nothing for right now
    }

    @Override
    public void onTestFailure(ITestResult result) {
        TestBase base = (TestBase) result.getInstance();
        // Plus 1 to match the Allure numbering
        String methodName = result.getMethod().getMethodName() + Integer.toString(result.getMethod().getParameterInvocationCount() + 1);
        try {
            base.attachPageSource(methodName);
            base.attachScreenshot(methodName);
            base.attachFailedLog(methodName);
        } catch (IOException e) {
            log.error("Error attaching file", e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Do nothing for right now
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Do nothing for right now
    }

    @Override
    public void onStart(ITestContext context) {
        // Do nothing for right now
    }

    @Override
    public void onFinish(ITestContext context) {
        // Do nothing for right now
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // Plus 1 to match the Allure numbering
        String methodName = method.getTestMethod().getMethodName() + Integer.toString(method.getTestMethod().getParameterInvocationCount() + 1);
        Thread.currentThread().setName(methodName);
        TestLogHelper.startTestLogging(methodName);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // Do nothing for right now
    }
}
