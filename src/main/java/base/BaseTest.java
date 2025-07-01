package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.ExtentReportManager;
import utils.LogUtil;
import utils.ScreenShotUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseTest {
    protected WebDriver driver;
    protected ExtentTest extentTest;
    private static final ExtentReports extentReports = ExtentReportManager.getInstance();
    
    @BeforeSuite
    public void beforeSuite(ITestContext context) {
        LogUtil.info("=== TEST SUITE STARTED: " + context.getSuite().getName() + " ===");
        LogUtil.info("Environment: " + ConfigReader.getEnvironment());
        LogUtil.info("Browser: " + ConfigReader.getBrowser());
        LogUtil.info("Parallel Execution: " + ConfigReader.isParallelExecution());
        LogUtil.info("Thread Count: " + ConfigReader.getThreadCount());
    }
    
    @BeforeTest
    public void beforeTest(ITestContext context) {
        LogUtil.info("=== TEST STARTED: " + context.getName() + " ===");
    }
    
    @BeforeMethod
    public void beforeMethod(Method method, ITestResult result) {
        String testName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        
        LogUtil.logTestStart(testName);
        
        // Create ExtentTest instance
        extentTest = extentReports.createTest(className + " - " + testName);
        extentTest.assignCategory(className);
        
        // Get WebDriver instance
        driver = DriverManager.getDriver();
        
        // Navigate to base URL
        String baseUrl = ConfigReader.getBaseUrl();
        LogUtil.logPageNavigation(baseUrl);
        driver.get(baseUrl);
        
        // Log test parameters if any
        if (result.getParameters().length > 0) {
            String params = Arrays.stream(result.getParameters())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            LogUtil.info("Test Parameters: " + params);
        }
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) {
        String testName = result.getName();
        String status = getTestStatus(result);
        
        LogUtil.logTestEnd(testName, status);
        
        // Handle test result in ExtentReports
        if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, "Test passed successfully");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
            
            // Capture screenshot on failure
            try {
                String screenshotPath = ScreenShotUtil.capture(driver, testName);
                extentTest.addScreenCaptureFromPath(screenshotPath);
                LogUtil.logScreenshot(screenshotPath);
            } catch (Exception e) {
                LogUtil.error("Failed to capture screenshot", e);
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(Status.SKIP, "Test skipped: " + result.getThrowable().getMessage());
        }
        
        // Quit driver
        DriverManager.quitDriver();
    }
    
    @AfterTest
    public void afterTest(ITestContext context) {
        LogUtil.info("=== TEST COMPLETED: " + context.getName() + " ===");
    }
    
    @AfterSuite
    public void afterSuite(ITestContext context) {
        LogUtil.info("=== TEST SUITE COMPLETED: " + context.getSuite().getName() + " ===");
        
        // Flush ExtentReports
        extentReports.flush();
        
        // Generate Allure report
        generateAllureReport();
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
    
    private void generateAllureReport() {
        try {
            LogUtil.info("Generating Allure report...");
            // Allure report generation is handled by Maven plugin
        } catch (Exception e) {
            LogUtil.error("Failed to generate Allure report", e);
        }
    }
    
    // Utility methods for tests
    protected void logStep(String step) {
        LogUtil.logStep(step);
        extentTest.log(Status.INFO, step);
    }
    
    protected void logInfo(String message) {
        LogUtil.info(message);
        extentTest.log(Status.INFO, message);
    }
    
    protected void logWarning(String message) {
        LogUtil.warn(message);
        extentTest.log(Status.WARNING, message);
    }
    
    protected void logError(String message) {
        LogUtil.error(message);
        extentTest.log(Status.FAIL, message);
    }
}
