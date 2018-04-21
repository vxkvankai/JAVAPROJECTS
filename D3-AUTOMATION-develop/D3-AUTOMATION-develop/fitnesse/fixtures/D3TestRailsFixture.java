package com.d3.testrails;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;
import fit.ColumnFixture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class D3TestRailsFixture extends ColumnFixture {

    // status_id 1 = Passed
    // 2 = Blocked
    // 4 = Retest
    // 5 = Failed

    public String TestRun;
    public String TestCase;
    JSONObject r;
    JSONArray a;
    public int actualStatus;
    public int expectedStatus;
    public String actualValue;
    public String expectedValue;
    public int milliseconds;


    public String SetTestRailResultByComparingRespCode() throws MalformedURLException, IOException, APIException {
        if (actualStatus == expectedStatus) {
            Passed();
            return "Passed";
        } else {
            Failed();
            return "Failed";
        }

    }

    public String SetTestRailResultByComparingJsonBody() throws MalformedURLException, IOException, APIException {
        if (actualValue.equals(expectedValue)) {
            Passed();
            return "Passed";
        } else {
            Failed();
            return "Failed";
        }

    }

    public void Passed() throws MalformedURLException, IOException, APIException {
        APIClient client = new APIClient("https://d3banking.testrail.com");
        client.setUser("qaadmin@d3banking.com");
        client.setPassword("D3B@nk!ngQA");
        Map data = new HashMap();
        data.put("status_id", new Integer(1));
        data.put("comment", "Test has passed");
        r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + TestCase, data);

    }

    public void Failed() throws MalformedURLException, IOException, APIException {
        APIClient client = new APIClient("https://d3banking.testrail.com");
        client.setUser("qaadmin@d3banking.com");
        client.setPassword("D3B@nk!ngQA");
        Map data = new HashMap();
        data.put("status_id", new Integer(5));
        data.put("comment", "Test has failed");
        r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + TestCase, data);

    }

    public String Retest() throws MalformedURLException, IOException, APIException {
        APIClient client = new APIClient("https://d3banking.testrail.com");
        client.setUser("qaadmin@d3banking.com");
        client.setPassword("D3B@nk!ngQA");
        Map data = new HashMap();
        data.put("status_id", new Integer(4));
        data.put("comment", "Test has been reset and ready to test");
        r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + TestCase, data);

        return "Retest Set";

    }

    public String RetestTestRun() throws MalformedURLException, IOException, APIException {
        JSONObject p;
        Map data = new HashMap();
        data.put("status_id", new Integer(4));
        APIClient client = new APIClient("https://d3banking.testrail.com/");
        client.setUser("qaadmin@d3banking.com");
        client.setPassword("D3B@nk!ngQA");

        a = (JSONArray) client.sendGet("get_tests/" + TestRun);

        for (Object anA : a) {
            p = (JSONObject) anA;
            r = (JSONObject) client.sendPost("add_result_for_case/" + TestRun + "/" + p.get("case_id"), data);
        }

        return "Test Run Reset";
    }

    public String Sleep() throws InterruptedException {
        Thread.sleep(milliseconds);
        return "Done Sleeping";
    }
}
