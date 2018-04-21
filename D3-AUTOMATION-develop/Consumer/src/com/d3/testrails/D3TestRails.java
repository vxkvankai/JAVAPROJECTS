package com.d3.testrails;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class D3TestRails {

    // status_id 1 = passed
    // 2 = Blocked
    // 4 = Retest
    // 5 = failed
    // 6 = KnownIssue

    private APIClient client;
    private String browse;

    private D3TestRails() {
        client = new APIClient(System.getProperty("testRailUrl"));
        client.setUser(System.getProperty("testRailUserName"));
        client.setPassword(System.getProperty("testRailPassWord"));
        browse = System.getProperty("browse");
    }

    /**
     * Send a test result to test rails
     *
     * @param testRun The test run id to send the result to
     * @param testCase Te test case id of the test that was tested
     * @param comment Will be added to the comment field in TestRails
     * @param statusId Result type to send: status_id 1 = passed, 2 = Blocked, 4 = Retest, 5 = failed, 6 = KnownIssue, 8 = flaky
     * @param issues Comma-separated list of defects to link to the test
     */
    private void sendTestResult(String testRun, String testCase, String comment, int statusId, @Nullable String issues)
            throws IOException, APIException {
        Map<String, Object> data = new HashMap<>();
        data.put("status_id", statusId);
        data.put("comment", comment);
        if (issues != null) {
            data.put("defects", issues);
        }
        client.sendPost("add_result_for_case/" + testRun + "/" + testCase, data);
    }

    /**
     * Send a test result to test rails
     *
     * @param testRun The test run id to send the result to
     * @param testCase Te test case id of the test that was tested
     * @param comment Will be added to the comment field in TestRails
     * @param statusId Result type to send: status_id 1 = passed, 2 = Blocked, 4 = Retest, 5 = failed, 6 = KnownIssue, 9 = flaky
     */
    private void sendTestResult(String testRun, String testCase, String comment, int statusId) throws IOException, APIException {
        sendTestResult(testRun, testCase, comment, statusId, null);
    }

    private void passed(String testRun, String testCase) throws IOException, APIException {
        String comment = String.format("Passed on Browser = %s", browse);
        sendTestResult(testRun, testCase, comment, 1);
    }

    private void retest(String testRun, String testCase) throws IOException, APIException {
        String comment = String.format("Skipped on Browser = %s!", browse);
        sendTestResult(testRun, testCase, comment, 4);
    }

    private void failed(String testRun, String testCase) throws IOException, APIException {
        String comment = String.format("Failed on Browser = %s!", browse);
        sendTestResult(testRun, testCase, comment, 5);
    }

    private void knownIssue(String testRun, String testCase, @Nonnull Issue[] issues) throws IOException, APIException {
        StringBuilder issueString = new StringBuilder();
        for (Issue issue : issues) {
            issueString.append(issue.value()).append(",");
        }

        String comment = String.format("Failed on Browser: %s, most likely due to bug #s: %s", browse, issueString);
        sendTestResult(testRun, testCase, comment, 6, issueString.toString());
    }

    private void flaky(String testRun, String testCase) throws IOException, APIException {
        String comment = "Test is marked as flaky, needs investigation";
        sendTestResult(testRun, testCase, comment, 9);
    }

    public static void updateTestRailsFlaky(TmsLink[] testCases, String testRun) {
        Logger logger = LoggerFactory.getLogger(D3TestRails.class);
        D3TestRails testRails = new D3TestRails();
        updateTestCasesToAutomated(testCases);

        // TODO add check if test case already has result
        for (TmsLink testCase : testCases) {
            try {
                testRails.flaky(testRun, testCase.value());
            } catch (IOException | APIException e) {
                logger.error("Error reporting TC number: {} to TestRail", testCase.value(), e);
            }
        }
    }

    public static void updateTestRails(@Nonnull TmsLink[] testCases, @Nonnull String testRun, @Nonnull ITestResult result, @Nullable Issue[] issues) {
        Logger logger = LoggerFactory.getLogger(D3TestRails.class);

        updateTestCasesToAutomated(testCases);
        logger.info("Reporting test results to TestRail");
        D3TestRails testRails = new D3TestRails();

        for (TmsLink testCase : testCases) {
            String testCaseNumber = testCase.value();
            try {
                int testStatus = result.getStatus();
                logger.info("Reporting Test Case: {}, TC number: {}", result.getMethod().getMethodName(), testCaseNumber);
                if (testStatus == ITestResult.SUCCESS) {
                    logger.info("Test case passed");
                    testRails.passed(testRun, testCaseNumber);
                } else if (testStatus == ITestResult.FAILURE) {
                    logger.error("Test case failed");
                    if (issues == null || issues.length == 0) {
                        testRails.failed(testRun, testCaseNumber);
                    } else {
                        testRails.knownIssue(testRun, testCaseNumber, issues);
                    }
                } else if (testStatus == ITestResult.SKIP) {
                    logger.warn("Test case was skipped");
                    testRails.retest(testRun, testCaseNumber);
                } else {
                    logger.warn("This status not configured specifically, marking as retest: Test Case: {}, TC number: {}",
                            result.getMethod().getMethodName(), testCaseNumber);
                    testRails.retest(testRun, testCaseNumber);
                }
            } catch (IOException | APIException e) {
                logger.error("Error reporting TC number: {} to TestRail", testCaseNumber, e);
            }
        }
    }

    private JSONObject getTestCaseDetails(String testCaseId) throws IOException, APIException {
        return (JSONObject) client.sendGet(String.format("get_case/%s", testCaseId));
    }

    private void updateTestCase(String testCaseId, Map<String, Object> data) throws IOException, APIException {
        client.sendPost(String.format("update_case/%s", testCaseId), data);
    }

    private static void updateTestCaseToAutomated(@Nonnull TmsLink testCase) {
        D3TestRails testRails = new D3TestRails();
        Logger logger = LoggerFactory.getLogger(D3TestRails.class);
        String id = testCase.value();
        try {
            logger.info("Checking if test case id {} is of type automated", id);
            if ((long) testRails.getTestCaseDetails(id).get("type_id") != 1L) {
                Map<String, Object> map = new HashMap<>();
                map.put("type_id", 1);
                testRails.updateTestCase(id, map);
                logger.info("Updated test case id {} to type automated", id);
            } else {
                logger.info("Test case id {} is already set as type automated", id);
            }
        } catch (APIException | IOException e) {
            logger.error("Error updating (or checking) TC to automated type: {} to TestRail", id, e);
        }
    }

    private static void updateTestCasesToAutomated(@Nonnull TmsLink[] testCases) {
        for (TmsLink testCase : testCases) {
            updateTestCaseToAutomated(testCase);
        }
    }
}
