package com.d3.support.webdrivers;


import org.openqa.selenium.Platform;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

public class InternetExplorerService extends DriverServiceManager {

    @Override
    public DriverService getAppiumDriverLocalService() {

        file = new File("drivers/windows/internetexplorer/64bit/IEDriverServer.exe");

        return new InternetExplorerDriverService.Builder()
                .usingDriverExecutable(file)
                .usingAnyFreePort()
                .build();
    }

    @Override
    public void stopService() {
        if (service != null && service.isRunning()) {
            service.stop();
            logger.info("INTERNET EXPLORER DRIVER SERVICE STOPPED");
        }
    }

    @Override
    protected DesiredCapabilities getCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities(BrowserType.IE, "11", Platform.WIN10);
        // Browserstack caps
        // TODO move these to a file maybe
        caps.setCapability("os", "WINDOWS");
        caps.setCapability("os_version", "10");
        caps.setCapability("browser_version", "11");
        caps.setCapability("resolution", "1680x1050");
        caps.setCapability("browserstack.selenium_version", "3.4.0");
        return caps;
    }
}
