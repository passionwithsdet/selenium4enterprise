package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import utils.ConfigReader;
import utils.LogUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    
    public static WebDriver createInstance() {
        String browser = ConfigReader.getBrowser();
        boolean isGridEnabled = ConfigReader.isGridEnabled();
        boolean isHeadless = ConfigReader.isHeadless();
        
        LogUtil.info("Creating WebDriver instance for browser: " + browser + 
                    ", Grid: " + isGridEnabled + ", Headless: " + isHeadless);
        
        try {
            if (isGridEnabled) {
                return createRemoteDriver(browser, isHeadless);
            } else {
                return createLocalDriver(browser, isHeadless);
            }
        } catch (Exception e) {
            LogUtil.error("Failed to create WebDriver instance", e);
            throw new RuntimeException("WebDriver creation failed", e);
        }
    }
    
    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return createChromeDriver(headless);
            case "firefox":
                return createFirefoxDriver(headless);
            case "edge":
                return createEdgeDriver(headless);
            case "safari":
                return createSafariDriver();
            default:
                LogUtil.warn("Unsupported browser: " + browser + ", defaulting to Chrome");
                return createChromeDriver(headless);
        }
    }
    
    private static WebDriver createRemoteDriver(String browser, boolean headless) throws MalformedURLException {
        String gridUrl = ConfigReader.getGridUrl();
        LogUtil.info("Creating remote WebDriver with grid URL: " + gridUrl);
        
        Capabilities capabilities = getCapabilities(browser, headless);
        WebDriver driver = new RemoteWebDriver(new URL(gridUrl), capabilities);
        
        setupDriver(driver);
        return driver;
    }
    
    private static ChromeDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Performance optimizations
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Mobile emulation if enabled
        if (ConfigReader.getBoolean("mobile.emulation", false)) {
            Map<String, Object> deviceMetrics = new HashMap<>();
            deviceMetrics.put("width", 375);
            deviceMetrics.put("height", 812);
            deviceMetrics.put("pixelRatio", 3.0);
            
            Map<String, Object> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceMetrics", deviceMetrics);
            mobileEmulation.put("userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15");
            
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }
        
        ChromeDriver driver = new ChromeDriver(options);
        setupDriver(driver);
        return driver;
    }
    
    private static FirefoxDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        
        FirefoxDriver driver = new FirefoxDriver(options);
        setupDriver(driver);
        return driver;
    }
    
    private static EdgeDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        
        EdgeDriver driver = new EdgeDriver(options);
        setupDriver(driver);
        return driver;
    }
    
    private static SafariDriver createSafariDriver() {
        WebDriverManager.safaridriver().setup();
        SafariOptions options = new SafariOptions();
        
        SafariDriver driver = new SafariDriver(options);
        setupDriver(driver);
        return driver;
    }
    
    private static Capabilities getCapabilities(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) chromeOptions.addArguments("--headless");
                return chromeOptions;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                return firefoxOptions;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless");
                return edgeOptions;
            default:
                ChromeOptions defaultOptions = new ChromeOptions();
                if (headless) defaultOptions.addArguments("--headless");
                return defaultOptions;
        }
    }
    
    private static void setupDriver(WebDriver driver) {
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait", 10)));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout", 30)));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        
        // Maximize window if not headless
        if (!ConfigReader.isHeadless()) {
            driver.manage().window().maximize();
        }
        
        LogUtil.info("WebDriver setup completed successfully");
    }
    
    public static void quitDriver(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
                LogUtil.info("WebDriver quit successfully");
            } catch (Exception e) {
                LogUtil.error("Error quitting WebDriver", e);
            }
        }
    }
}
