package com.d3.support.webdrivers;

public class DriverFactory {

    private DriverFactory() {
        throw new IllegalStateException("Utility Class");
    }

    public static DriverServiceManager getService(String browser){

        DriverServiceManager service;

        switch(browser.toUpperCase()){
            case "FIREFOX":
                service = new FirefoxService();
                break;
            case "IE":
                service = new InternetExplorerService();
                break;
            case "CHROME":
                service = new ChromeService();
                break;
            case "SAFARI":
                service = new SafariService();
                break;
            case "EDGE":
                service = new EdgeService();
                break;
            case "MOBILE":
                service = new AppiumService();
                break;
            default:
                throw new IllegalArgumentException("The following is not a valid DriverService : " + browser);
        }
        return service;

    }
}
