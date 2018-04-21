package com.d3.tests.listeners;

import com.d3.api.helpers.banking.D3BankingApi;
import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.exceptions.D3ApiException;
import com.d3.testrails.D3TestRails;
import io.qameta.allure.Flaky;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.testng.IAlterSuiteListener;
import org.testng.IAnnotationTransformer2;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class ProjectListener implements IAnnotationTransformer2, IAlterSuiteListener {

    private static final String BROWSE_PROP = "browse";
    private static final String CLIENT = "client";
    private static final String ENVIRONMENT = "environment";
    private static final String MOBILE_BROWSER = "mobileBrowserType";
    private static final String PARALLEL_PROP = "parallel";
    private static final String PROPERTY_FILE = "propertyFile";
    private static final String REPORT_TO_TEST_RAIL = "reportToTestRail";
    private static final String TEST_RAIL_RUN = "testRailRun";
    private static final String TEST_RUN_CASES_ONLY = "testRunCasesOnly";
    private static List<String> testCases = new ArrayList<>();


    /**
     * Set System properties from the given properties files
     */
    public ProjectListener() {
        // set default values for propertyFile and environment properties
        setDefaultPropertyValue(PROPERTY_FILE, "test.properties");
        setDefaultPropertyValue(ENVIRONMENT, "coreqaa");

        String environmentFile = String.format("%s.properties", System.getProperty(ENVIRONMENT));

        addSystemProperties(System.getProperty(PROPERTY_FILE));
        addSystemProperties(environmentFile);

        setDefaultPropertyValue(BROWSE_PROP, "CHROME");
        setDefaultPropertyValue(MOBILE_BROWSER, "NONE");
        setDefaultPropertyValue(TEST_RUN_CASES_ONLY, "false");

        log.info("Browser set to: {}", System.getProperty(BROWSE_PROP));
        setTestRailRun();
        getOnlyTestCasesFromTestRun();

    }

    /**
     * If PARALLEL_PROP is set, this will enable/disable the `launchBrowser` and 'launchBrowserPerTest` method in the TestBase class accordingly.
     * When browserForEach System property is set to "method", launchBrowserPerTest will be enabled and launchBrowser will be disabled
     * browserForEach will default to "class" if not found or null, which will disable launchBrowserPerTest and enable launchBrowser
     */
    @Override
    public void transform(IConfigurationAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (System.getProperty(PARALLEL_PROP) != null) {
            String methodName = testMethod.getName();
            boolean browserForEachMethod = (Optional.ofNullable(System.getProperty("browserForEach"))
                .filter(property -> !property.isEmpty())
                .orElse("class").equalsIgnoreCase("method"));

            if (methodName.endsWith("PerTest") && browserForEachMethod) {
                annotation.setEnabled(true);

            } else if (methodName.endsWith("Browser") && browserForEachMethod) {
                annotation.setEnabled(false);
            }
        }
    }

    private boolean isFlaky(Method testMethod) {
        return testMethod.isAnnotationPresent(Flaky.class);
    }

    private boolean testRunCasesOnly() {
        return System.getProperty(TEST_RUN_CASES_ONLY).equalsIgnoreCase("true");
    }

    private boolean testRunIncludesCaseId(Method testMethod) {
        TmsLink[] links = testMethod.getAnnotationsByType(TmsLink.class);
        for (TmsLink link : links) {
            if (testCases.contains(link.value())) {
                String caseId = link.value();
                log.info("Test run includes case id: {{}}. Will execute method {{}}.", caseId, testMethod.getName());
                return true;
            }
        }

        return false;
    }

    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {
        // Do nothing, not currently used
    }

    @Override
    public void transform(IFactoryAnnotation annotation, Method method) {
        // Do nothing, not currently used
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (isFlaky(testMethod)) {
            log.warn("{} is set as flaky, ignoring", testMethod.getName());
            annotation.setEnabled(false);
            updateTestRailFlaky(testMethod);
        }
        // disable any test methods that don't have test case matching id in the set testRailRun
        if (testRunCasesOnly() && !testRunIncludesCaseId(testMethod)) {
            log.warn("Test run did not include any case id(s) that matched @TmsLink(s) associated to method {{}}, ignoring", testMethod.getName());
            annotation.setEnabled(false);
        }
    }

    private void updateTestRailFlaky(Method testMethod) {
        String reportToTestRails = System.getProperty(REPORT_TO_TEST_RAIL, "false");
        TmsLink[] links = testMethod.getAnnotationsByType(TmsLink.class);
        String testRun = System.getProperty(TEST_RAIL_RUN, "");

        if (reportToTestRails != null && reportToTestRails.equalsIgnoreCase("true") && links.length != 0 && !testRun.isEmpty()) {
            D3TestRails.updateTestRailsFlaky(links, testRun);
        } else {
            log.info("Not reporting to TestRail: reportToTestRail: {}, tmsLinks length: {}, testRun: {}", reportToTestRails, links.length, testRun);
        }
    }

    /**
     * If not running on core this will add client specific packages to the suite xml before the test run
     * ex: <package name="com.d3.tests.consumer.{client}.*" />
     *
     * @param suites XML Suite being run
     */
    @Override
    public void alter(List<XmlSuite> suites) {
        XmlSuite suite = suites.get(0);
        if (System.getProperty(PARALLEL_PROP) != null) {
            XmlSuite.ParallelMode mode =
                System.getProperty(PARALLEL_PROP).equalsIgnoreCase("classes") ? XmlSuite.ParallelMode.CLASSES : XmlSuite.ParallelMode.METHODS;
            suite.setParallel(mode);
        }

        if (!System.getProperty(CLIENT).equals("core")) {
            log.info("Not running core. Client currently set to: {}", System.getProperty(CLIENT));
            String clientPackage = String.format("com.d3.tests.consumer.%s.*", System.getProperty(CLIENT));
            suite.getTests().get(0).getXmlPackages().add(new XmlPackage(clientPackage));
        } else {
            log.info("Running Core. Not adding additional client package to XML");
        }
    }

    /**
     * Adds to the system properties from a given configuration file name
     *
     * @param configFileName name of the config file found in [user.dir]/src/main/resources
     */

    private void addSystemProperties(String configFileName) {
        log.info("Attempting to load file: {} into properties", configFileName);
        try (FileInputStream envConf = new FileInputStream(
            String.format("%s/src/main/resources/%s", System.getProperty("user.dir"), configFileName))) {
            Properties envProperties = new Properties();
            envProperties.load(envConf);

            for (String prop : envProperties.stringPropertyNames()) {
                System.setProperty(prop, envProperties.getProperty(prop));
            }
        } catch (IOException e) {
            log.error("Issue adding the system properties from file", e);
        }
    }

    /**
     * If system property is null or empty, this will set the property to the given value
     *
     * @param property property name
     * @param defaultValue value to set for property
     */
    private void setDefaultPropertyValue(String property, String defaultValue) {
        if (System.getProperty(property) == null || System.getProperty(property).isEmpty()) {
            log.info("PROPERTY: {} not set, defaulting to {}", property, defaultValue);
            System.setProperty(property, defaultValue);
        }
    }

    /**
     * Will set testRailRun property if not set and reportToTestRail is set to true
     */
    private void setTestRailRun() {
        String testRun = System.getProperty(TEST_RAIL_RUN, "");
        String reportToTestRail = System.getProperty(REPORT_TO_TEST_RAIL, "");
        if (testRun.isEmpty() && reportToTestRail.equalsIgnoreCase("true")) {
            log.info("Current Test Rail Run is not set");
            int testRunId = 0;
            String version = null;
            try {
                // no real reason to use money movement here, just needs to be on the api
                D3BankingApi api = new MoneyMovementApiHelper(System.getProperty("fi1"));
                version = api.getApiVersion();
                testRunId = D3TestRails.getTestRunId(version, System.getProperty(CLIENT));
            } catch (D3ApiException e) {
                log.error("Error getting test run id");
            }
            if (testRunId != 0) {
                System.setProperty(TEST_RAIL_RUN, String.valueOf(testRunId));
                log.info("Current Test Rail Run is set to: {}", System.getProperty(TEST_RAIL_RUN));
            } else {
                log.warn("No Test Run found for VERSION: {} and CLIENT: {}. Not setting TEST_RUN id.", version, System.getProperty(CLIENT));
            }
        }
    }

    private void getOnlyTestCasesFromTestRun() {
        String testRun = System.getProperty(TEST_RAIL_RUN, "");
        String testRunCasesOnly = System.getProperty(TEST_RUN_CASES_ONLY, "");
        if (!testRunCasesOnly.isEmpty() && System.getProperty(TEST_RUN_CASES_ONLY).equalsIgnoreCase("true") && !testRun.isEmpty()) {
            log.info("{} is set to true, getting only tests cases included in test run with id {}", TEST_RUN_CASES_ONLY, testRun);
            List<String> testCasesInRun = D3TestRails.getAllTestCasesInRun(Integer.parseInt(System.getProperty(TEST_RAIL_RUN)));
            testCases.addAll(testCasesInRun);
        }
    }
}
