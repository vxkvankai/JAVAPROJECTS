package com.d3.testrails;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
public class D3TestRails {

    private static final int PASSED = 1;
    private static final int RETEST = 4;
    private static final int FAILED = 5;
    private static final int KNOWN_ISSUE = 6;
    private static final int FLAKY = 9;
    private static final int PROJECT_ID = 14; // D3 Quality Assurance Project

    private APIClient client;
    private String browse;

    private D3TestRails() {
        client = new APIClient(System.getProperty("testRailUrl"));
        client.setUser(System.getProperty("testRailUserName"));
        client.setPassword(System.getProperty("testRailPassWord"));
        browse = System.getProperty("browse");
    }

    public static void updateTestRailsFlaky(TmsLink[] testCases, String testRun) {
        D3TestRails testRails = new D3TestRails();
        updateTestCasesToAutomated(testCases);

        // TODO add check if test case already has result
        for (TmsLink testCase : testCases) {
            try {
                testRails.flaky(testRun, testCase.value());
            } catch (IOException | APIException e) {
                log.error("Error reporting TC number: {} to TestRail", testCase.value(), e);
            }
        }
    }

    public static void updateTestRails(@Nonnull TmsLink[] testCases, @Nonnull String testRun, @Nonnull ITestResult result, @Nullable Issue[] issues) {
        updateTestCasesToAutomated(testCases);
        log.info("Reporting test results to TestRail");
        D3TestRails testRails = new D3TestRails();

        for (TmsLink testCase : testCases) {
            String testCaseNumber = testCase.value();
            try {
                int testStatus = result.getStatus();
                log.info("Reporting Test Case: {}, TC number: {}", result.getMethod().getMethodName(), testCaseNumber);
                if (testStatus == ITestResult.SUCCESS) {
                    log.info("Test case passed");
                    testRails.passed(testRun, testCaseNumber);
                } else if (testStatus == ITestResult.FAILURE) {
                    log.error("Test case failed");
                    if (issues == null || issues.length == 0) {
                        testRails.failed(testRun, testCaseNumber);
                    } else {
                        testRails.knownIssue(testRun, testCaseNumber, issues);
                    }
                } else if (testStatus == ITestResult.SKIP) {
                    log.warn("Test case was skipped");
                    testRails.retest(testRun, testCaseNumber);
                } else {
                    log.warn("This status not configured specifically, marking as retest: Test Case: {}, TC number: {}",
                        result.getMethod().getMethodName(), testCaseNumber);
                    testRails.retest(testRun, testCaseNumber);
                }
            } catch (IOException | APIException e) {
                log.error("Error reporting TC number: {} to TestRail", testCaseNumber, e);
            }
        }
    }

    private static void updateTestCaseToAutomated(@Nonnull TmsLink testCase) {
        D3TestRails testRails = new D3TestRails();
        String id = testCase.value();
        try {
            log.info("Checking if test case id {} is of type automated", id);
            if ((long) testRails.getTestCaseDetails(id).get("type_id") != 1L) {
                Map<String, Object> map = new HashMap<>();
                map.put("type_id", 1);
                testRails.updateTestCase(id, map);
                log.info("Updated test case id {} to type automated", id);
            } else {
                log.info("Test case id {} is already set as type automated", id);
            }
        } catch (APIException | IOException e) {
            log.error("Error updating (or checking) TC to automated type: {} to TestRail", id, e);
        }
    }

    private static void updateTestCasesToAutomated(@Nonnull TmsLink[] testCases) {
        for (TmsLink testCase : testCases) {
            updateTestCaseToAutomated(testCase);
        }
    }

    /**
     * This will get the parent milestone id for the specified release version
     * NOTE: api call getMilestones() does not return sub-milestones, only parent
     *
     * @param releaseVersion release version of milestone (ex: 3.3.1, 3.4.0)
     * @return id of parent milestone. Will return 0 if no parent milestone found for given release version
     */
    private static int getParentMilestoneId(String releaseVersion) {
        D3TestRails testRails = new D3TestRails();
        releaseVersion = StringUtils.substringBefore(releaseVersion, "-");
        JSONObject milestones = null;
        try {
            milestones = testRails.getArrayObjectContainingKeyValue(testRails.getMilestones(), "name", releaseVersion);
        } catch (IOException | APIException e) {
            log.error("Error getting milestone for release version {}", releaseVersion, e);
            return 0;
        }

        return milestones != null ? Integer.parseInt(milestones.get("id").toString()) : 0;
    }

    /**
     * This will get the test run id for the specified release version and client
     *
     * @param releaseVersion version of code current test environment is on
     * @param client current test environment client (ex: core, tcf, demo, etc.)
     * @return test run id for specific release version and client. Will return 0 if no test run was found matching release version and client
     */
    public static int getTestRunId(String releaseVersion, String client) {
        D3TestRails testRails = new D3TestRails();
        JSONArray testRuns = null;
        try {
            JSONObject parentMilestone = testRails.getMilestone(getParentMilestoneId(releaseVersion));
            testRuns = testRails.getTestRunsForMilestone(testRails.getSubMilestoneId(parentMilestone, client));
        } catch (IOException | APIException e) {
            log.error("Error getting test run for version: {}, and client: {}", releaseVersion, client, e);
            return 0;
        }
        JSONObject arrayObj = testRails.getArrayObjectContainingKeyValue(testRuns, "name", releaseVersion);
        return arrayObj != null ? Integer.parseInt(arrayObj.get("id").toString()) : 0;
    }

    /**
     * This will return a list of all test case ids found in the specified run
     *
     * @param testRunId id of test run to get test cases from
     * @return list of test case ids found
     */
    public static List<String> getAllTestCasesInRun(int testRunId) {
        D3TestRails testRails = new D3TestRails();
        List<String> listOfTestCaseIds = new ArrayList<>();
        JSONArray testCases;
        try {
            testCases = testRails.getTestsForRun(testRunId);
        } catch (IOException | APIException e) {
            log.error("Error getting tests for test run {}", testRunId, e);
            return Collections.emptyList();
        }
        for (Object test : testCases) {
            String caseId = ((JSONObject) test).get("case_id").toString();
            log.info("Found test case in run with id: {}", caseId);
            listOfTestCaseIds.add(caseId);
        }
        return listOfTestCaseIds;
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
     * @param testCase The test case id of the test that was tested
     * @param comment Will be added to the comment field in TestRails
     * @param statusId Result type to send: status_id 1 = passed, 2 = Blocked, 4 = Retest, 5 = failed, 6 = KnownIssue, 9 = flaky
     */
    private void sendTestResult(String testRun, String testCase, String comment, int statusId) throws IOException, APIException {
        sendTestResult(testRun, testCase, comment, statusId, null);
    }

    private void passed(String testRun, String testCase) throws IOException, APIException {
        String comment = String.format("Passed on Browser = %s", browse);
        sendTestResult(testRun, testCase, comment, PASSED);
    }

    private void retest(String testRun, String testCase) throws IOException, APIException {
        String comment = String.format("Skipped on Browser = %s!", browse);
        sendTestResult(testRun, testCase, comment, RETEST);
    }

    private void failed(String testRun, String testCase) throws IOException, APIException {
        String comment = String.format("Failed on Browser = %s!", browse);
        sendTestResult(testRun, testCase, comment, FAILED);
    }

    private void knownIssue(String testRun, String testCase, @Nonnull Issue[] issues) throws IOException, APIException {
        StringBuilder issueString = new StringBuilder();
        for (Issue issue : issues) {
            issueString.append(issue.value()).append(",");
        }

        String comment = String.format("Failed on Browser: %s, most likely due to bug #s: %s", browse, issueString);
        sendTestResult(testRun, testCase, comment, KNOWN_ISSUE, issueString.toString());
    }

    private void flaky(String testRun, String testCase) throws IOException, APIException {
        String comment = "Test is marked as flaky, needs investigation";
        sendTestResult(testRun, testCase, comment, FLAKY);
    }

    private JSONObject getTestCaseDetails(String testCaseId) throws IOException, APIException {
        return (JSONObject) client.sendGet(String.format("get_case/%s", testCaseId));
    }

    private void updateTestCase(String testCaseId, Map<String, Object> data) throws IOException, APIException {
        client.sendPost(String.format("update_case/%s", testCaseId), data);
    }

    private JSONArray getTestRunsForMilestone(int milestoneId) throws IOException, APIException {
        D3TestRails testRails = new D3TestRails();
        return (JSONArray) testRails.client.sendGet(String.format("get_runs/%s&milestone_id=%s", PROJECT_ID, milestoneId));
    }

    private JSONArray getMilestones() throws IOException, APIException {
        D3TestRails testRails = new D3TestRails();
        return (JSONArray) testRails.client.sendGet(String.format("get_milestones/%s", PROJECT_ID));
    }

    private JSONArray getTestsForRun(int testRunId) throws IOException, APIException {
        D3TestRails testRails = new D3TestRails();
        return (JSONArray) testRails.client.sendGet(String.format("get_tests/%s", testRunId));
    }

    private JSONObject getMilestone(int milestoneId) throws IOException, APIException {
        D3TestRails testRails = new D3TestRails();
        return (JSONObject) testRails.client.sendGet(String.format("get_milestone/%s", milestoneId));
    }

    /**
     * This will get a JSONArray based on the key provided
     *
     * @param object JSONObject to get array from
     * @param key Name of key to get
     * @return JSONArray
     */
    private JSONArray getJsonArray(JSONObject object, String key) {
        return (JSONArray) object.get(key);
    }

    /**
     * This will get an Object from a JSONArray where the given key contains the given value
     *
     * @param array JSONArray to get object from
     * @param key Name of Key to check for
     * @param value Value to match for Key
     * @return JSONObject where value matches for given key
     */
    private JSONObject getArrayObjectContainingKeyValue(JSONArray array, String key, String value) {
        JSONObject arrayObject = null;
        for (Object obj : array) {
            if (((JSONObject) obj).get(key).toString().toLowerCase().contains(value)) {
                arrayObject = (JSONObject) obj;
                break;
            }
        }
        return arrayObject;
    }

    /**
     * This will get the id of a sub-milestone given the parent milestone and specified client
     *
     * @param parentMilestone parent milestone of release version
     * @param client Client sub-milestone to get id for
     * @return sub-milestone id for specified client. Will return 0 if no sub-milestone found for given client
     */
    private int getSubMilestoneId(JSONObject parentMilestone, String client) {
        JSONArray subMilestones = getJsonArray(parentMilestone, "milestones");
        JSONObject arrayObj = getArrayObjectContainingKeyValue(subMilestones, "name", client);
        return arrayObj != null ? Integer.parseInt(arrayObj.get("id").toString()) : 0;
    }
}
