package com.d3.helpers;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class TestLogHelper {
    private static final String TEST_NAME_STR = "testname";

    private TestLogHelper() {
    }

    /**
     * Start thread context test logging
     *
     * @param name Name of the test
     */
    public static void startTestLogging(String name) {
        log.info("Starting logging for {}", name);
        MDC.put(TEST_NAME_STR, name);
    }

    /**
     * Stop thread context test logging
     *
     * @return Name of the test
     */
    public static void stopTestLogging() {
        String name = MDC.get(TEST_NAME_STR);
        log.info("Stopping logging for {}", name);
        MDC.remove(TEST_NAME_STR);
    }
}
