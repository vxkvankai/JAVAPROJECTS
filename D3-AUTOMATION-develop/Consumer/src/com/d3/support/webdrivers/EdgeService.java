package com.d3.support.webdrivers;

import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

public class EdgeService extends DriverServiceManager {

    @Override
    public DriverService getAppiumDriverLocalService() {

        file = new File(".\\libs\\MicrosoftWebDriver.exe");

        return new EdgeDriverService.Builder()
                .usingDriverExecutable(file)
                .usingAnyFreePort()
                .build();
    }

    @Override
    public void stopService() {
        if (service != null && service.isRunning()) {
            service.stop();
            logger.info("EDGE DRIVER SERVICE STOPPED");
        }
    }

    @Override
    protected DesiredCapabilities getCapabilities() {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy("eager");
        DesiredCapabilities capabilities = DesiredCapabilities.edge();
        capabilities.setCapability(EdgeOptions.CAPABILITY, options);
        capabilities.setCapability("resolution", "1680x1050");
        return capabilities;
    }
}