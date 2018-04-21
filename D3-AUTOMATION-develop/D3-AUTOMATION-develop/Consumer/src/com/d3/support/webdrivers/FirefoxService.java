package com.d3.support.webdrivers;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

@Slf4j
public class FirefoxService extends DriverServiceManager {

    @Override
    public DriverService getAppiumDriverLocalService() {
        switch (os) {
            case "mac os x":
                file = new File("drivers/osx/marionette/64bit/geckodriver");
                break;
            case "linux":
                file = new File("drivers/linux/marionette/64bit/geckodriver");
                break;
            default:
                file = new File("drivers/windows/marionette/64bit/geckodriver.exe");
                break;
        }
        return new GeckoDriverService.Builder()
                .usingDriverExecutable(file)
                .usingAnyFreePort()
                .build();
    }

    @Override
    public void stopService() {
        if (service != null && service.isRunning()) {
            service.stop();
        }
        log.info("FIREFOX DRIVER SERVICE STOPPED");
    }

    @Override
    protected DesiredCapabilities getCapabilities() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("focusmanager.testmode", true);
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        return capabilities;
    }
}