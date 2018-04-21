package com.d3.tests.listeners;

import com.d3.testrails.D3TestRails;
import io.qameta.allure.Flaky;
import io.qameta.allure.TmsLink;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ProjectListener implements IAnnotationTransformer2, IAlterSuiteListener {

    private static final String BROWSE_PROP = "browse";
    private static final String CLIENT = "client";
    private static final String MOBILE_BROWSER = "mobileBrowserType";
    private static final String PARALLEL_PROP = "parallel";

    /**
     * Set System properties from the given properties files
     */
    public ProjectListener() {
        String configFile = System.getProperty("propertyFile", "test.properties");
        String environment = System.getProperty("environment", "coreqab");

        // default to coreqaa if environment property is empty instead of not set
        environment = environment.isEmpty() ? "coreqab" : environment;
        String environmentFile = String.format("%s.properties", environment);

        addSystemProperties(configFile);
        addSystemProperties(environmentFile);

        if (System.getProperty(BROWSE_PROP) == null || System.getProperty(BROWSE_PROP).isEmpty()) {
            LoggerFactory.getLogger(ProjectListener.class).info("Browser property not set, defaulting to Chrome");
            System.setProperty(BROWSE_PROP, "CHROME");
        }

        LoggerFactory.getLogger(ProjectListener.class).info("Browser set to: {}", System.getProperty(BROWSE_PROP));

        if (System.getProperty(MOBILE_BROWSER) == null || System.getProperty(MOBILE_BROWSER).isEmpty()) {
            System.setProperty(MOBILE_BROWSER, "NONE");
        }
    }

    /**
     * If PARALLEL_PROP is set, this will enable/disable the `launchBrowser` and 'launchBrowserPerTest` method in the TestBase class accordingly.
     * When browserForEach System property is set to "method", launchBrowserPerTest will be enabled and launchBrowser will be disabled
     * browserForEach will default to "class" if not found or null, which will disable launchBrowserPerTest and enable launchBrowser
     *
     * @param annotation
     * @param testClass
     * @param testConstructor
     * @param testMethod
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
            LoggerFactory.getLogger(getClass()).warn("{} is set as flaky, ignoring", testMethod.getName());
            annotation.setEnabled(false);
            updateTestRailFlaky(testMethod);
        }
    }

    private void updateTestRailFlaky(Method testMethod) {
        String reportToTestRails = System.getProperty("reportToTestRail", "false");
        TmsLink[] links = testMethod.getAnnotationsByType(TmsLink.class);
        String testRun = System.getProperty("testRailRun", "");

        if (reportToTestRails != null && reportToTestRails.equalsIgnoreCase("true") && links.length != 0 && !testRun.isEmpty()) {
            D3TestRails.updateTestRailsFlaky(links, testRun);
        } else {
            LoggerFactory.getLogger(getClass()).info("Not reporting to TestRail: reportToTestRail: {}, tmsLinks length: {}, testRun: {}", reportToTestRails, links.length,
                    testRun);
        }
    }



    /**
     * If not running on core this will add client specific packages to the suite xml before the test run
     * ex: <package name="com.d3.tests.consumer.{client}.*" />
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
            LoggerFactory.getLogger(ProjectListener.class).info("Not running core. Client currently set to: {}", System.getProperty(CLIENT));
            String clientPackage = String.format("com.d3.tests.consumer.%s.*", System.getProperty(CLIENT));
            suite.getTests().get(0).getXmlPackages().add(new XmlPackage(clientPackage));
        } else {
            LoggerFactory.getLogger(ProjectListener.class).info("Running Core. Not adding additional client package to XML");
        }
    }

    /**
     * Adds to the system properties from a given configuration file name
     *
     * @param configFileName name of the config file found in [user.dir]/src/main/resources
     */

    private void addSystemProperties(String configFileName) {
        LoggerFactory.getLogger(ProjectListener.class).info("Attempting to load file: {} into properties", configFileName);
        try (FileInputStream envConf = new FileInputStream(
                String.format("%s/src/main/resources/%s", System.getProperty("user.dir"), configFileName))) {
            Properties envProperties = new Properties();
            envProperties.load(envConf);

            for (String prop : envProperties.stringPropertyNames()) {
                System.setProperty(prop, envProperties.getProperty(prop));
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(ProjectListener.class).error("Issue adding the system properties from file", e);
        }
    }
}

