package driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.ConfigReader;
import utils.LogUtil;

import java.net.URL;
import java.time.Duration;

public class MobileDriverFactory {
    private static AppiumDriverLocalService appiumService;
    private static AppiumDriver driver;
    
    public static AppiumDriver createInstance() {
        String platform = ConfigReader.get("mobile.platform", "android");
        boolean isRealDevice = ConfigReader.getBoolean("mobile.real.device", false);
        
        LogUtil.info("Creating Mobile Driver for platform: " + platform + 
                    ", Real Device: " + isRealDevice);
        
        try {
            // Start Appium service if not running
            startAppiumService();
            
            if ("android".equalsIgnoreCase(platform)) {
                return createAndroidDriver(isRealDevice);
            } else if ("ios".equalsIgnoreCase(platform)) {
                return createIOSDriver(isRealDevice);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
        } catch (Exception e) {
            LogUtil.error("Failed to create Mobile Driver", e);
            throw new RuntimeException("Mobile Driver creation failed", e);
        }
    }
    
    private static AndroidDriver createAndroidDriver(boolean isRealDevice) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Basic capabilities
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", "org.wikipedia.main.MainActivity");
        
        // Device capabilities
        if (isRealDevice) {
            capabilities.setCapability("deviceName", ConfigReader.get("mobile.device.name", "Android Device"));
            capabilities.setCapability("udid", ConfigReader.get("mobile.device.udid", ""));
        } else {
            capabilities.setCapability("deviceName", ConfigReader.get("mobile.emulator.name", "Pixel_6_API_30"));
            capabilities.setCapability("avd", ConfigReader.get("mobile.emulator.avd", "Pixel_6_API_30"));
        }
        
        // App capabilities
        String appPath = ConfigReader.get("mobile.app.path", "");
        if (!appPath.isEmpty()) {
            capabilities.setCapability("app", appPath);
        }
        
        // Additional capabilities
        capabilities.setCapability("noReset", ConfigReader.getBoolean("mobile.no.reset", false));
        capabilities.setCapability("fullReset", ConfigReader.getBoolean("mobile.full.reset", true));
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("newCommandTimeout", 60);
        
        try {
            AndroidDriver androidDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            setupDriver(androidDriver);
            return androidDriver;
        } catch (Exception e) {
            LogUtil.error("Failed to create Android Driver", e);
            throw new RuntimeException("Android Driver creation failed", e);
        }
    }
    
    private static IOSDriver createIOSDriver(boolean isRealDevice) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Basic capabilities
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("bundleId", "org.wikipedia.Wikipedia");
        
        // Device capabilities
        if (isRealDevice) {
            capabilities.setCapability("deviceName", ConfigReader.get("mobile.device.name", "iPhone"));
            capabilities.setCapability("udid", ConfigReader.get("mobile.device.udid", ""));
        } else {
            capabilities.setCapability("deviceName", ConfigReader.get("mobile.simulator.name", "iPhone 14"));
            capabilities.setCapability("platformVersion", ConfigReader.get("mobile.simulator.version", "16.0"));
        }
        
        // App capabilities
        String appPath = ConfigReader.get("mobile.app.path", "");
        if (!appPath.isEmpty()) {
            capabilities.setCapability("app", appPath);
        }
        
        // Additional capabilities
        capabilities.setCapability("noReset", ConfigReader.getBoolean("mobile.no.reset", false));
        capabilities.setCapability("fullReset", ConfigReader.getBoolean("mobile.full.reset", true));
        capabilities.setCapability("autoAcceptAlerts", true);
        capabilities.setCapability("newCommandTimeout", 60);
        
        try {
            IOSDriver iosDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            setupDriver(iosDriver);
            return iosDriver;
        } catch (Exception e) {
            LogUtil.error("Failed to create iOS Driver", e);
            throw new RuntimeException("iOS Driver creation failed", e);
        }
    }
    
    private static void setupDriver(AppiumDriver driver) {
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Log device information
        LogUtil.info("Platform: " + driver.getCapabilities().getCapability("platformName"));
        LogUtil.info("Device Name: " + driver.getCapabilities().getCapability("deviceName"));
        LogUtil.info("Automation Name: " + driver.getCapabilities().getCapability("automationName"));
        
        LogUtil.info("Mobile Driver setup completed successfully");
    }
    
    private static void startAppiumService() {
        if (appiumService == null || !appiumService.isRunning()) {
            AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder()
                    .usingPort(4723)
                    .withIPAddress("127.0.0.1");
            
            appiumService = AppiumDriverLocalService.buildService(serviceBuilder);
            appiumService.start();
            
            LogUtil.info("Appium service started on: " + appiumService.getUrl());
        }
    }
    
    public static void stopAppiumService() {
        if (appiumService != null && appiumService.isRunning()) {
            appiumService.stop();
            LogUtil.info("Appium service stopped");
        }
    }
    
    public static void quitDriver(AppiumDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
                LogUtil.info("Mobile Driver quit successfully");
            } catch (Exception e) {
                LogUtil.error("Error quitting Mobile Driver", e);
            }
        }
    }
    
    // Utility methods for device management
    public static boolean isEmulatorRunning(String avdName) {
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            // Add logic to check if emulator is running
            return true;
        } catch (Exception e) {
            LogUtil.error("Error checking emulator status", e);
            return false;
        }
    }
    
    public static void startEmulator(String avdName) {
        try {
            String androidHome = System.getenv("ANDROID_HOME");
            String emulatorPath = androidHome + "/emulator/emulator";
            Process process = Runtime.getRuntime().exec(emulatorPath + " -avd " + avdName);
            LogUtil.info("Starting emulator: " + avdName);
        } catch (Exception e) {
            LogUtil.error("Error starting emulator", e);
        }
    }
} 