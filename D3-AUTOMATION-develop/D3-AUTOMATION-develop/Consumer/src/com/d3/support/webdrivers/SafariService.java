package com.d3.support.webdrivers;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariOptions;

import java.io.IOException;

@Slf4j
public class SafariService extends DriverServiceManager {

    private String safariDriver = System.getProperty("safariDriver");

    @Override
    public DriverService getAppiumDriverLocalService() {

        int port = 8888;
        String[] command = new String[] {safariDriver, "-p", Integer.toString(port)};

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            log.error("Issue starting Safari Service", e);
            return null;
        }

        return new SafariDriverService.Builder()
                .usingPort(port)
                .build();
    }

    @Override
    public void stopService() {
        if (service != null && service.isRunning()) {
            service.stop();
            log.info("SAFARI DRIVER SERVICE STOPPED");
        }
    }

    @Override
    protected DesiredCapabilities getCapabilities() {
        SafariOptions options = new SafariOptions();
        options.setUseCleanSession(options.getUseCleanSession());
        DesiredCapabilities capabilities = new DesiredCapabilities(BrowserType.SAFARI, "9.1", Platform.EL_CAPITAN);
        capabilities.setCapability(SafariOptions.CAPABILITY, options);
        // Browserstack caps
        // TODO move these to a file maybe
        capabilities.setCapability("os", "OS X");
        capabilities.setCapability("os_version", "El Capitan");
        capabilities.setCapability("browser_version", "9.1");
        capabilities.setCapability("resolution", "1920x1080");
        capabilities.setCapability("browserstack.safari.allowAllCookies", "true");
        return capabilities;
    }
}