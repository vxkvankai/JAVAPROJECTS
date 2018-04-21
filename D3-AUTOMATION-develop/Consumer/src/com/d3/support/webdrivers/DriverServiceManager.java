package com.d3.support.webdrivers;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public abstract class DriverServiceManager {

    //    protected ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>(); <---- For Parallel Testing
    protected DriverService service;
    protected File file;
    protected WebDriver driver;
    protected boolean isMobile = false;
    protected Logger logger;
    protected Long timeout;
    protected String browse;
    protected String os;
    private boolean useRemoteDriver = false;
    private URL driverUrl;

    protected abstract void stopService();

    protected abstract DesiredCapabilities getCapabilities();

    protected abstract DriverService getAppiumDriverLocalService();

    public DriverServiceManager() {
        os = System.getProperty("os.name").toLowerCase();
        logger = LoggerFactory.getLogger(getClass().getName());
        browse = System.getProperty("browse");
        timeout = Long.valueOf(System.getProperty("WebdriverTimeout"));
        useRemoteDriver = "true".equalsIgnoreCase(System.getProperty("useRemoteDriver"));
        if (useRemoteDriver) {
            try {
                driverUrl = getHubUrl();
            } catch (MalformedURLException e) {
                logger.error("Issue with hubUrl, falling back to local Selenium", e);
                useRemoteDriver = false;
            }
        }
    }

    private URL getHubUrl() throws MalformedURLException {
        return new URL(System.getProperty("hubUrl", "http://localhost:4444/wd/hub"));
    }

    void startService() throws IOException {
        if (service == null && !useRemoteDriver) {
            logger.info("Starting service");
            service = getAppiumDriverLocalService();
            service.start();
            driverUrl = service.getUrl();
        } else {
            logger.info("useRemoteDriver set to True, not starting driver service");
        }
    }

    public void killBrowser() {
        try {
            if (browse.contains("APP")) {
                ((AppiumDriver<?>) driver).closeApp();
            } else if (driver != null) {
                driver.quit();
                driver = null;
            } else {
                logger.warn("WebDriver object is null");
            }
        } catch (Exception e) {
            logger.error("Issue killing the browser", e);
            driver = null;
        }
    }

    public WebDriver getWebDriver() {
        try {
            startService();
        } catch (IOException e) {
           logger.error("Issue starting Service, driver not created", e);
            return null;
        }

        DesiredCapabilities capabilities = getCapabilities();
        capabilities.setCapability("browserTimeout", 60);

        // sauce labs capabilities
        capabilities.setCapability("commandTimeout", 60);
        capabilities.setCapability("idleTimeout", 90);

        capabilities.setCapability("recordVideo", false);

        // local/tunnel config for browserstack
        if (System.getProperty("needsLocalConnection", "false").equalsIgnoreCase("true")) {
            logger.info("Setting caps to use local tunneling");
            String localIdent = System.getProperty("localIdentifier", "zalenium");
            logger.info("local identifier: {}", localIdent);
            capabilities.setCapability("browserstack.local", true);
            capabilities.setCapability("browserstack.localIdentifier", localIdent);
        }

        driver = new RemoteWebDriver(driverUrl, capabilities);

        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        return driver;
    }

    public void shutdown() {
        stopService();
    }
}