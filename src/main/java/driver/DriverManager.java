package driver;

import org.openqa.selenium.WebDriver;
import utils.LogUtil;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            LogUtil.info("Creating new WebDriver instance for thread: " + Thread.currentThread().getName());
            driverThread.set(DriverFactory.createInstance());
        }
        return driverThread.get();
    }
    
    public static void setDriver(WebDriver driver) {
        LogUtil.info("Setting WebDriver instance for thread: " + Thread.currentThread().getName());
        driverThread.set(driver);
    }
    
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            LogUtil.info("Quitting WebDriver for thread: " + Thread.currentThread().getName());
            DriverFactory.quitDriver(driver);
            driverThread.remove();
        }
    }
    
    public static void removeDriver() {
        driverThread.remove();
    }
    
    public static boolean hasDriver() {
        return driverThread.get() != null;
    }
}
