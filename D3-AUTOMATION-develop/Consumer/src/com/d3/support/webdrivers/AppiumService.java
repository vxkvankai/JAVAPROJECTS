package com.d3.support.webdrivers;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AppiumService extends DriverServiceManager {

    private AppiumDriverLocalService appiumDriverLocalService;
    private String androidApp = System.getProperty("androidApp");
    private String bundleId = System.getProperty("bundleId");
    private String appPackage = System.getProperty("appPackage");
    private String appActivity = System.getProperty("appAcitivity");
    private String appWaitActivity = System.getProperty("appWaitActivity");
    private String keychainPassword = System.getProperty("keychainPassword");
    private String deviceName = System.getProperty("deviceName");
    private String platformName = System.getProperty("platformName");
    private String platformVersion = System.getProperty("platformVersion");
    private String udid = System.getProperty("udid");
    private String mobileBrowserType = System.getProperty("mobileBrowserType");

    public DriverService getAppiumDriverLocalService() {
        DesiredCapabilities serverCapabilities = new DesiredCapabilities();
        serverCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        serverCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        serverCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        serverCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        serverCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
        serverCapabilities.setCapability(MobileCapabilityType.UDID, udid);

        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .withCapabilities(serverCapabilities)
                .withIPAddress(System.getProperty("appiumServiceIp", "127.0.0.1")) // NOSONAR
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE));
    }

    @Override
    public void stopService() {
        if (appiumDriverLocalService != null && appiumDriverLocalService.isRunning()) {
            appiumDriverLocalService.stop();
            logger.info("APPIUM SERVICE STOPPED");
        }
    }

    @Override
    protected DesiredCapabilities getCapabilities() {
        logger.warn("getCapabilities not supported with AppiumService");
        return null;
    }

    @Override
    public WebDriver getWebDriver() {
        try {
            startService();
        } catch (IOException e) {
            logger.error("Issue starting Service, driver not created", e);
            return null;
        }

        isMobile = true;
        switch (mobileBrowserType) {
            case "IOS_APP":
                File xcodeconfig = new File("apps/WebDriverAgent.xcconfig");

                DesiredCapabilities iosApp = new DesiredCapabilities();
                iosApp.setCapability(IOSMobileCapabilityType.BUNDLE_ID, bundleId);
                iosApp.setCapability(IOSMobileCapabilityType.SEND_KEY_STRATEGY, "setValue");
                iosApp.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
                iosApp.setCapability(IOSMobileCapabilityType.NATIVE_INSTRUMENTS_LIB, true);
                iosApp.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                if (System.getProperty("IsLocal") == null) {
                    iosApp.setCapability("keychainPath", "/Users/jenkins/Library/Keychains/QAAutomation.keychain-db");
                    iosApp.setCapability("keychainPassword", keychainPassword);
                }
                iosApp.setCapability("xcodeConfigFile", xcodeconfig.getAbsolutePath());

                try {
                    driver = new IOSDriver<>(new URL(appiumDriverLocalService.getUrl().toString()), iosApp);
                } catch (MalformedURLException e) {
                    logger.error(e.toString());
                }
                break;
            case "IOS_WEB":

                xcodeconfig = new File("apps/WebDriverAgent.xcconfig");
                DesiredCapabilities iosWeb = new DesiredCapabilities();
                iosWeb.setCapability(MobileCapabilityType.BROWSER_NAME, "safari");
                iosWeb.setCapability(IOSMobileCapabilityType.BUNDLE_ID, bundleId);
                iosWeb.setCapability(IOSMobileCapabilityType.SEND_KEY_STRATEGY, "setValue");
                iosWeb.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
                iosWeb.setCapability(IOSMobileCapabilityType.NATIVE_INSTRUMENTS_LIB, true);
                iosWeb.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                iosWeb.setCapability("realDeviceLogger", "idevicesyslog");
                iosWeb.setCapability("xcodeConfigFile", xcodeconfig.getAbsolutePath());
                try {
                    driver = new IOSDriver<MobileElement>(new URL(appiumDriverLocalService.getUrl().toString()), iosWeb);
                } catch (MalformedURLException e) {
                    logger.error(e.toString());
                }
                break;
            case "ANDROID_WEB":
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
                try {
                    driver = new RemoteWebDriver(new URL(appiumDriverLocalService.getUrl().toString()), caps);
                } catch (MalformedURLException e) {
                    logger.error(e.toString());
                }
                break;
            case "ANDROID_APP":
                File app = new File(androidApp);
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
                cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
                cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
                cap.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, appWaitActivity);
                try {
                    driver = new AndroidDriver<MobileElement>(new URL(appiumDriverLocalService.getUrl().toString()), cap);
                } catch (MalformedURLException e) {
                    logger.error(e.toString());
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return driver;
    }
}