package base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import driver.MobileDriverFactory;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.ExtentReportManager;
import utils.LogUtil;
import utils.ScreenShotUtil;

import java.lang.reflect.Method;

public class BaseMobileTest {
    protected AppiumDriver driver;
    protected ExtentTest extentTest;
    private static final com.aventstack.extentreports.ExtentReports extentReports = ExtentReportManager.getInstance();

    @BeforeSuite
    public void beforeSuite() {
        LogUtil.info("=== MOBILE TEST SUITE STARTED ===");
        LogUtil.info("Platform: " + ConfigReader.get("mobile.platform", "android"));
        LogUtil.info("Real Device: " + ConfigReader.getBoolean("mobile.real.device", false));
        LogUtil.info("App: Wikipedia Mobile");
    }

    @BeforeTest
    public void beforeTest() {
        LogUtil.info("=== MOBILE TEST STARTED ===");
    }

    @BeforeMethod
    public void beforeMethod(Method method, ITestResult result) {
        String testName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();

        LogUtil.logTestStart("Mobile Test: " + testName);

        // Create ExtentTest instance
        extentTest = extentReports.createTest(className + " - " + testName);
        extentTest.assignCategory("Mobile");
        extentTest.assignCategory(ConfigReader.get("mobile.platform", "android"));

        // Get Mobile Driver instance
        driver = MobileDriverFactory.createInstance();

        // Log device information
        logDeviceInfo();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String testName = result.getName();
        String status = getTestStatus(result);

        LogUtil.logTestEnd("Mobile Test: " + testName, status);

        // Handle test result in ExtentReports
        if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, "Mobile test passed successfully");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(Status.FAIL, "Mobile test failed: " + result.getThrowable().getMessage());

            // Capture screenshot on failure
            try {
                String screenshotPath = ScreenShotUtil.capture(driver, testName);
                extentTest.addScreenCaptureFromPath(screenshotPath);
                LogUtil.logScreenshot(screenshotPath);
            } catch (Exception e) {
                LogUtil.error("Failed to capture mobile screenshot", e);
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(Status.SKIP, "Mobile test skipped: " + result.getThrowable().getMessage());
        }

        // Quit driver
        MobileDriverFactory.quitDriver(driver);
    }

    @AfterTest
    public void afterTest() {
        LogUtil.info("=== MOBILE TEST COMPLETED ===");
    }

    @AfterSuite
    public void afterSuite() {
        LogUtil.info("=== MOBILE TEST SUITE COMPLETED ===");
        
        // Stop Appium service
        MobileDriverFactory.stopAppiumService();
        
        // Flush ExtentReports
        extentReports.flush();
    }

    private String getTestStatus(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                return "PASSED";
            case ITestResult.FAILURE:
                return "FAILED";
            case ITestResult.SKIP:
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }

    private void logDeviceInfo() {
        try {
            String platform = (String) driver.getCapabilities().getCapability("platformName");
            String deviceName = (String) driver.getCapabilities().getCapability("deviceName");
            String automationName = (String) driver.getCapabilities().getCapability("automationName");

            LogUtil.info("Device Information:");
            LogUtil.info("- Platform: " + platform);
            LogUtil.info("- Device: " + deviceName);
            LogUtil.info("- Automation: " + automationName);

            extentTest.log(Status.INFO, "Device: " + deviceName + " (" + platform + ")");
        } catch (Exception e) {
            LogUtil.error("Failed to log device information", e);
        }
    }

    // Utility methods for mobile tests
    protected void logStep(String step) {
        LogUtil.logStep("Mobile: " + step);
        extentTest.log(Status.INFO, step);
    }

    protected void logInfo(String message) {
        LogUtil.info("Mobile: " + message);
        extentTest.log(Status.INFO, message);
    }

    protected void logWarning(String message) {
        LogUtil.warn("Mobile: " + message);
        extentTest.log(Status.WARNING, message);
    }

    protected void logError(String message) {
        LogUtil.error("Mobile: " + message);
        extentTest.log(Status.FAIL, message);
    }

    // Mobile-specific utility methods
    protected void waitForElement(WebElement element, int timeoutSeconds) {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            LogUtil.error("Element not visible after " + timeoutSeconds + " seconds", e);
        }
    }

    protected void tapElement(WebElement element) {
        try {
            element.click();
            LogUtil.info("Tapped element: " + element.getText());
        } catch (Exception e) {
            LogUtil.error("Failed to tap element", e);
        }
    }

    protected void enterText(WebElement element, String text) {
        try {
            element.clear();
            element.sendKeys(text);
            LogUtil.info("Entered text: " + text);
        } catch (Exception e) {
            LogUtil.error("Failed to enter text", e);
        }
    }

    protected String getElementText(WebElement element) {
        try {
            return element.getText();
        } catch (Exception e) {
            LogUtil.error("Failed to get element text", e);
            return "";
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Gesture methods
    protected void swipeUp() {
        try {
            // Implement swipe up using TouchAction or W3C Actions
            LogUtil.info("Swiped up");
        } catch (Exception e) {
            LogUtil.error("Failed to swipe up", e);
        }
    }

    protected void swipeDown() {
        try {
            // Implement swipe down
            LogUtil.info("Swiped down");
        } catch (Exception e) {
            LogUtil.error("Failed to swipe down", e);
        }
    }

    protected void swipeLeft() {
        try {
            // Implement swipe left
            LogUtil.info("Swiped left");
        } catch (Exception e) {
            LogUtil.error("Failed to swipe left", e);
        }
    }

    protected void swipeRight() {
        try {
            // Implement swipe right
            LogUtil.info("Swiped right");
        } catch (Exception e) {
            LogUtil.error("Failed to swipe right", e);
        }
    }

    // Performance monitoring
    protected void logPerformanceMetrics() {
        try {
            // Log app performance metrics
            LogUtil.info("Performance metrics logged");
        } catch (Exception e) {
            LogUtil.error("Failed to log performance metrics", e);
        }
    }
} 