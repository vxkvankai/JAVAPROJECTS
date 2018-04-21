package com.d3.tests;

import static com.d3.helpers.DateAndCurrencyHelper.getDateWithDashes;
import static com.d3.helpers.WebdriverHelper.isMobileApp;

import com.d3.api.helpers.banking.D3BankingApi;
import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.company.CompanyHelper;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.AccountHelper;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.headers.Header;
import com.d3.pages.consumer.login.LoginPage;
import com.d3.pages.consumer.termsofservice.TermsOfService;
import com.d3.support.webdrivers.DriverFactory;
import com.d3.support.webdrivers.DriverServiceManager;
import com.d3.testrails.D3TestRails;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.DataRequiresCompanyAttributes;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import com.d3.tests.annotations.RunWithCompanyAttributes;
import com.d3.tests.dataproviders.DataProviderBase;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.javatuples.Pair;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class TestBase {

    private static final String BROWSE_PROP = "browse";
    private static final String API_VERSION = "bankingApiUrl";
    private static final String CLIENT = "client";
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected String testDataFilename = "";
    private DriverServiceManager driverService;

    /**
     * Gets the Consumer API Url from the System properties (fi and bankingApiUrl)
     *
     * @param fi Fi property name to get the url for (fi1, fi2, etc)
     */
    public static String getConsumerApiURLFromProperties(String fi) {
        return System.getProperty(fi) + System.getProperty(API_VERSION);
    }

    /**
     * Gets the Consumer API url without the api version from the system properties
     *
     * @param fi Fi property name to get the url for (fi1, fi2, etc)
     */
    public static String getConsumerBaseUrl(String fi) {
        return System.getProperty(fi);
    }

    /**
     * Gets the Consumer API version without the base url from the system properties
     */
    public static String getConsumerApiVersion() {
        return System.getProperty(API_VERSION);
    }

    /**
     * Gets API version from the test environment and sets company attributes for the different FIs
     */
    @BeforeSuite(alwaysRun = true)
    public void configureCompanyAttributes(ITestContext context) throws ConduitException, D3ApiException {
        if (System.getProperty(API_VERSION) == null) {
            // no real reason to use money movement here, just needs to be on the api
            D3BankingApi api = new MoneyMovementApiHelper(System.getProperty("fi1"));
            System.setProperty(API_VERSION, String.format("d3rest/v%s/", api.getApiVersion()));
        }

        logger.info("Current API Version is set to: {}", System.getProperty(API_VERSION));

        //Only change company attribute configuration (Toggle, OOB, etc.) settings when running on core
        if (System.getProperty(CLIENT).equals("core")) {
            CompanyHelper.setupCompanyAttributes("fi1", getConsumerBaseUrl("fi1"), getConsumerApiVersion());
        }

        DatabaseUtils.updateCompanyAttributeValueString(CompanyAttribute.ON_US_ROUTING_NUMBERS,
            StringUtils.join(AccountHelper.getOnUsRoutingNumbers(), ","));

        logger.info("Removing any conduit files from failed tests");
        File[] files = new File("conduit/").listFiles(f -> f.getName().endsWith(".xml"));
        if (files == null) {
            logger.error("Error finding conduit files to delete");
            return;
        }

        //noinspection ResultOfMethodCallIgnored
        Arrays.stream(files).forEach(File::delete);
    }

    /**
     * Set up the retry logic for each test See the retryCount property to edit the amount of times retry will occur
     */
    @BeforeSuite(alwaysRun = true, dependsOnMethods = {"configureCompanyAttributes"})
    public void setupRetryLogic(ITestContext context) {
        for (ITestNGMethod method : context.getAllTestMethods()) {
            method.setRetryAnalyzer(new RetryAnalyzerImpl());
        }
    }

    /**
     * Goes through all the tests, gathers the data provider names, then runs through the D3DataProviders of the same name.
     */
    @BeforeSuite(alwaysRun = true, dependsOnMethods = {"setupRetryLogic"})
    public void setupData(ITestContext context) {
        // Holds the mapping from test name to data for the test
        ConcurrentHashMap<String, Pair<Object[][], String>> methodToDataMap = new ConcurrentHashMap<>();
        ExecutorService concurrentService = Executors.newFixedThreadPool(3);
        Map<String, Pair<Future<Object[][]>, String>> testDataThreads = new HashMap<>();

        // Go through each test method being run
        for (ITestNGMethod method : context.getAllTestMethods()) {

            // Get the underlying method and test annotation
            Method reflectMethod = method.getConstructorOrMethod().getMethod();

            Test testAnnotation = reflectMethod.getAnnotation(Test.class);

            // Get the data provider name
            String dataProviderName = testAnnotation.dataProvider();

            // get d3 data provider methods

            // Load the data provider classes
            Reflections reflections = new Reflections("com.d3.tests.dataproviders");
            Set<Class<? extends DataProviderBase>> allDataProviderClasses = reflections.getSubTypesOf(DataProviderBase.class);
            boolean foundProvider = false;

            // loop through the classes to try and find the correct d3 dataprovider
            for (Class dataProviderClass : allDataProviderClasses) {

                // loop through the methods looking for the right provider
                foundProvider =
                    matchDataProviderMethodsAndAddToMap(dataProviderName, testDataThreads, reflectMethod, concurrentService, dataProviderClass);

                if (foundProvider) {
                    break;
                }
            }

            if (!foundProvider) {
                logger.error(
                    "Error adding the data provider data or none were found. method name: {}. Check to make sure the @D3DataProvider annotation is set.",
                    method.getMethodName());
            }
        }

        concurrentService.shutdown();

        for (Map.Entry<String, Pair<Future<Object[][]>, String>> testData : testDataThreads.entrySet()) {
            try {
                // Call the get method on the data provider method, and store the function itself into the map
                methodToDataMap.putIfAbsent(testData.getKey(), new Pair<>(testData.getValue().getValue0().get(), testData.getValue().getValue1()));
            } catch (InterruptedException e) {
                logger.error("Creating test data thread was interrupted", e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                logger.error("Execution error when creating test data", e);
            }
        }

        writeSerializedMapToFile(methodToDataMap);
    }

    /**
     * Loops through a class's methods and adds a Future object to the given map to the name of the test.
     *
     * @param dataProviderName Name of the data Provider to find
     * @param testDataThreads Map to modify to add the test data to
     * @param reflectMethod Method that was reflected (of the test)
     * @param concurrentService Executor Service to make the threads from
     * @param dataProviderClass Class of the Data Provider is located in
     * @return True if found/added to the map, false otherwise
     */
    private boolean matchDataProviderMethodsAndAddToMap(String dataProviderName, Map<String, Pair<Future<Object[][]>, String>> testDataThreads,
        Method reflectMethod, ExecutorService concurrentService, Class dataProviderClass) {

        Method[] allClassMethods = dataProviderClass.getMethods();
        for (Method classMethod : allClassMethods) {
            D3DataProvider d3DataProvider = classMethod.getAnnotation(D3DataProvider.class);

            // if there is match between the test data provider name and the d3 data provider name, continue
            if (d3DataProvider != null && d3DataProvider.name().equals(dataProviderName)) {

                // Call the method and put the results into the test data map with the name of the test
                logger.info("Calling the data provider method: {}", classMethod.getName());
                Callable<Object[][]> dataFunction = () -> (Object[][]) classMethod.invoke(dataProviderClass.newInstance(), reflectMethod);

                // Dont run data provider if it will be run later in the order
                if (reflectMethod.isAnnotationPresent(DataRequiresCompanyAttributes.class)) {
                    logger.info("Not running dataprovider due to @DataRequiresCompanyAttributes");
                    Pair<Future<Object[][]>, String> pair = new Pair<>(concurrentService.submit(() -> new Object[][] {{}}),
                        classMethod.getDeclaringClass().getName() + "#" + classMethod.getName());

                    testDataThreads.put(reflectMethod.getClass().getName() + "." + reflectMethod.getName(), pair);
                } else {
                    Pair<Future<Object[][]>, String> pair =
                        new Pair<>(concurrentService.submit(dataFunction), classMethod.getDeclaringClass().getName() + "#" + classMethod.getName());
                    testDataThreads.put(reflectMethod.getClass().getName() + "." + reflectMethod.getName(), pair);
                }

                return true;
            }
        }
        return false;
    }

    /**
     * Write a HashMap of the test data to a file
     *
     * @param map HashMap that has the data to be written
     */
    private void writeSerializedMapToFile(ConcurrentHashMap<String, Pair<Object[][], String>> map) {
        // Serialize data object to file
        File dir = new File("testDataDir");
        if (!dir.exists() && dir.mkdir()) {
            logger.info("Created testDataDir directory");
        }

        testDataFilename = String.format("testDataDir/testData%s.ser", RandomHelper.getRandomString(10));
        System.setProperty("testDataFilename", testDataFilename);
        logger.info("Saving data serialized object to {}", testDataFilename);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testDataFilename))) {
            oos.writeObject(map);
            oos.flush();
        } catch (FileNotFoundException e) {
            logger.error("Error opening file {} to write serialized data", testDataFilename, e);
        } catch (IOException e) {
            logger.error("Error writing to file {}", testDataFilename, e);
        }
    }

    /**
     * Setup the Driver service
     */
    @BeforeClass(alwaysRun = true)
    public void setupDriverService() {
        driverService = DriverFactory.getService(System.getProperty(BROWSE_PROP));
    }

    /**
     * Main setup base method. This method initializes the webdriver, any DB connections, as well as the Test Rails configuration
     */
    @BeforeClass(alwaysRun = true, dependsOnMethods = {"setupDriverService"})
    @Parameters({"WebdriverTimeout"})
    public void launchBrowser(String webdriverTimeout) {
        launchBrowserSetup();
    }

    /**
     * Add a company attribute from the Company Attribute Annotation
     */
    @BeforeMethod(alwaysRun = true)
    public void addCompanyAttribute(Method method) {
        if (method.isAnnotationPresent(RunWithCompanyAttributes.class) || method.isAnnotationPresent(RunWithCompanyAttribute.class)) {
            Arrays.stream(method.getAnnotationsByType(RunWithCompanyAttribute.class)).forEach(this::addCompanyAttribute);
        }
    }

    /**
     * Adds a company attribute from an annotation
     *
     * @param annotation Annotation that has the needed information
     */
    private void addCompanyAttribute(RunWithCompanyAttribute annotation) {
        if (annotation.value().isEmpty()) {
            DatabaseUtils.updateCompanyAttributeValueString(annotation.attribute().getDefinition(), annotation.enabled());
        } else {
            DatabaseUtils.updateCompanyAttributeValueString(annotation.attribute().getDefinition(), annotation.value());
        }
    }

    private void resetCompanyAttribute(RunWithCompanyAttribute annotation) {
        if (annotation.resetAfterTest() && annotation.value().isEmpty()) {
            DatabaseUtils.updateCompanyAttributeValueString(annotation.attribute().getDefinition(), !annotation.enabled());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void resetCompanyAttribute(Method method) {
        if (method.isAnnotationPresent(RunWithCompanyAttributes.class) || method.isAnnotationPresent(RunWithCompanyAttribute.class)) {
            Arrays.stream(method.getAnnotationsByType(RunWithCompanyAttribute.class)).forEach(this::resetCompanyAttribute);
        }
    }

    /**
     * Launches a new browser for each test Need to have AnnotationTransform listener added to the suite xml And have browserForEach property set to
     * 'method' in the test.properties file Otherwise will launch browser per class by default
     */
    @BeforeMethod(alwaysRun = true, enabled = false)
    @Parameters({"WebdriverTimeout"})
    public void launchBrowserPerTest(String webdriverTimeout) {
        launchBrowserSetup();
    }

    /**
     * Logs out of the app when the test is done
     *
     * @param context Test context
     */
    @AfterMethod(alwaysRun = true, dependsOnMethods = "sendToTestRails")
    public void driverLogout(ITestContext context) {
        Header header = Header.initialize(driver, Header.class);
        try {
            if (isMobileApp()) {
                header.clickLogout();
            }
        } catch (Exception e) {
            logger.error("Issue logging out after test: {} with exception\n{}", context.getName(), e.toString());
        }
    }

    /**
     * Send the results of the test to Test Rails
     *
     * @param result The result of the test
     * @param method The Method being tested
     */
    @AfterMethod(alwaysRun = true)
    public void sendToTestRails(ITestResult result, Method method) {

        Issue[] issues = method.getAnnotationsByType(Issue.class);
        if (issues.length != 0) {
            String message;
            if (result.isSuccess()) {
                message = "Issue: {} is marked on this test, but the test is passing";
            } else {
                message = "This test is probably failing due to Issue: {}";
            }
            for (Issue issue : issues) {
                if (logger.isWarnEnabled()) {
                    logger.warn(message, issue.value());
                }
            }
        }

        String reportToTestRails = System.getProperty("reportToTestRail", "false");
        TmsLink[] links = method.getAnnotationsByType(TmsLink.class);
        Issue[] issuesTags = method.getAnnotationsByType(Issue.class);
        String testRun = System.getProperty("testRailRun", "");
        if (reportToTestRails != null && reportToTestRails.equalsIgnoreCase("true") && links.length != 0 && !testRun.isEmpty()) {
            D3TestRails.updateTestRails(links, testRun, result, issuesTags);
        } else {
            logger.info("Not reporting to TestRail: reportToTestRail: {}, tmsLinks length: {}, testRun: {}", reportToTestRails, links.length,
                testRun);
        }
    }

    /**
     * Take a screenshot on test failure
     *
     * @param result The result of the test
     */
    @AfterMethod(alwaysRun = true)
    public void takeScreenshot(ITestResult result, Method method) {
        try {
            byte[] screenshot;
            if (driver != null && !result.isSuccess()) {
                // (JMoravec 06/08/17): Sometimes get weird hangups on taking screenshots with no errors given.
                // This is an attempt to add a timeout to the command
                Callable<byte[]> callable = () -> takeScreenshot(method.getName());
                ExecutorService service = Executors.newCachedThreadPool();
                Future<byte[]> task = service.submit(callable);
                screenshot = task.get(10, TimeUnit.SECONDS);
                Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
            }
        } catch (Exception e) {
            logger.error("Error taking screenshot", e);
        }
    }

    /**
     * Kills the browser at the end of a class test run
     */
    @AfterClass(alwaysRun = true)
    public void terminateBrowser() {
        driverService.killBrowser();
    }

    /**
     * Kills the browser at the end of each individual test Need to have AnnotationTransform listener added to the suite xml And have browserForEach
     * property set to 'method' in the test.properties file Otherwise browser will be terminated at the end of each Class
     */
    @AfterMethod(alwaysRun = true, enabled = false)
    public void terminateBrowserPerTest() {
        driverService.killBrowser();
    }


    @AfterSuite(alwaysRun = true)
    public void generateEnvironmentPropertiesForAllure() {
        List<String> listOfWantedProperties = Arrays.asList(BROWSE_PROP, API_VERSION, CLIENT, "fi1", "fi2", "fi3");
        Properties systemProps = System.getProperties();
        Properties props = new Properties();
        for (String prop : listOfWantedProperties) {
            props.setProperty(prop, systemProps.getProperty(prop, ""));
        }
        try {
            File f = new File("allure-results/environment.properties");
            //noinspection ResultOfMethodCallIgnored
            f.getParentFile().mkdirs();
            if (f.createNewFile()) {
                props.store(new FileOutputStream(f), "Allure Environment Properties");
            } else {
                logger.error("Error creating allure env file");
            }
        } catch (IOException e) {
            logger.error("Error creating allure env file", e);
        }
    }

    /**
     * Stops the driver service after the suite has finished
     */
    @AfterSuite(alwaysRun = true)
    public void stopDriverService() {
        if (driverService != null) {
            logger.warn("Driver Service was null");
            driverService.shutdown();
        }
    }

    /**
     * Common launch browser code
     */
    private void launchBrowserSetup() {
        driver = driverService.getWebDriver();
        loginPage = LoginPage.initialize(driver, LoginPage.class);
    }

    /**
     * Takes a screenshot of the current webdriver and saves it
     *
     * @param fileName Filename of the screenshot (method appends date and .png to end)
     */
    public byte[] takeScreenshot(String fileName) throws IOException {
        logger.info("Taking Screenshot for test: {}", fileName);
        String screenshotFolder = "Reports/Screenshots/";
        if (System.getProperty(BROWSE_PROP).contains("APP")) {
            ((AppiumDriver<?>) driver).context("NATIVE_APP");
        }
        String screenShotName = screenshotFolder + fileName + "-" + getDateWithDashes() + ".png";
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        FileUtils.writeByteArrayToFile(new File(screenShotName), screenshot);
        return screenshot;
    }

    @Step("Login as user: {user.login}/{user.password}")
    protected Dashboard login(D3User user) {
        loginPage.setBaseUrl(getConsumerBaseUrl(user.getCuID()));
        loginPage.setApiVersion(getConsumerApiVersion());
        try {
            return loginPage.login(user);
        } catch (D3ApiException e) {
            logger.error("Error logging in", e);
            return null;
        }
    }

    @Step("Login manually (with TOS not yet accepted) as {user.login}/{user.password}")
    protected TermsOfService loginTOS(D3User user) throws D3ApiException {
        loginPage.setBaseUrl(getConsumerBaseUrl(user.getCuID()));
        loginPage.setApiVersion(getConsumerApiVersion());
        return loginPage.loginTOS(user);
    }
}

