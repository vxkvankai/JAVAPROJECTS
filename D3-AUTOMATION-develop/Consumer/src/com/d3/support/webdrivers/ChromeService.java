package com.d3.support.webdrivers;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

public class ChromeService extends DriverServiceManager {

    @Override
    public DriverService getAppiumDriverLocalService() {
        switch (os) {
            case "mac os x":
                file = new File("drivers/osx/googlechrome/64bit/chromedriver");
                break;
            case "linux":
                file = new File("drivers/linux/googlechrome/64bit/chromedriver");
                break;
            default:
                file = new File("drivers/windows/googlechrome/64bit/chromedriver.exe");
                break;
        }

        return new ChromeDriverService.Builder()
                .usingDriverExecutable(file)
                .usingAnyFreePort()
                .build();
    }

    @Override
    public void stopService() {
        if (service != null && service.isRunning()) {
            service.stop();
            logger.info("CHROME DRIVER SERVICE STOPPED");
        }
    }

    @Override
    protected DesiredCapabilities getCapabilities() {
        return DesiredCapabilities.chrome();
    }
}