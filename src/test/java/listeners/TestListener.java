package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;
import utils.LogUtil;
import utils.ScreenShotUtil;

import java.lang.reflect.Method;

public class TestListener implements ITestListener {
    private static final ExtentReports extentReports = ExtentReportManager.getInstance();
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        String testName = method.getName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        
        LogUtil.logTestStart(testName);
        
        // Create ExtentTest instance
        ExtentTest test = extentReports.createTest(className + " - " + testName);
        extentTest.set(test);
        
        // Add test parameters
        if (result.getParameters().length > 0) {
            for (int i = 0; i < result.getParameters().length; i++) {
                test.assignCategory("Parameter-" + (i + 1) + ": " + result.getParameters()[i]);
            }
        }
        
        // Add test groups
        if (result.getMethod().getGroups().length > 0) {
            test.assignCategory(result.getMethod().getGroups());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getName();
        LogUtil.logTestEnd(testName, "PASSED");
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
            test.log(Status.INFO, "Execution time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getName();
        LogUtil.logTestEnd(testName, "FAILED");
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
            
            // Capture screenshot on failure
            try {
                WebDriver driver = getDriverFromResult(result);
                if (driver != null) {
                    String screenshotPath = ScreenShotUtil.capture(driver, testName);
                    test.addScreenCaptureFromPath(screenshotPath);
                    LogUtil.logScreenshot(screenshotPath);
                }
            } catch (Exception e) {
                LogUtil.error("Failed to capture screenshot", e);
            }
            
            // Log stack trace
            test.log(Status.FAIL, "Stack trace: " + result.getThrowable().getStackTrace());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getName();
        LogUtil.logTestEnd(testName, "SKIPPED");
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getName();
        LogUtil.logTestEnd(testName, "FAILED_WITH_SUCCESS_PERCENTAGE");
        
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.WARNING, "Test failed but within success percentage");
        }
    }

    @Override
    public void onStart(ITestContext context) {
        LogUtil.info("=== TEST SUITE STARTED: " + context.getName() + " ===");
        LogUtil.info("Parallel execution: " + context.getCurrentXmlTest().getParallel());
        LogUtil.info("Thread count: " + context.getCurrentXmlTest().getThreadCount());
    }

    @Override
    public void onFinish(ITestContext context) {
        LogUtil.info("=== TEST SUITE COMPLETED: " + context.getName() + " ===");
        LogUtil.info("Total tests: " + context.getAllTestMethods().length);
        LogUtil.info("Passed: " + context.getPassedTests().size());
        LogUtil.info("Failed: " + context.getFailedTests().size());
        LogUtil.info("Skipped: " + context.getSkippedTests().size());
        
        // Flush ExtentReports
        extentReports.flush();
    }

    private WebDriver getDriverFromResult(ITestResult result) {
        try {
            // Try to get driver from test instance
            Object testInstance = result.getInstance();
            if (testInstance != null) {
                // Check if test class has a driver field
                try {
                    java.lang.reflect.Field driverField = testInstance.getClass().getDeclaredField("driver");
                    driverField.setAccessible(true);
                    Object driver = driverField.get(testInstance);
                    if (driver instanceof WebDriver) {
                        return (WebDriver) driver;
                    }
                } catch (NoSuchFieldException e) {
                    // Field not found, try alternative approaches
                }
            }
        } catch (Exception e) {
            LogUtil.error("Failed to get driver from test result", e);
        }
        return null;
    }

    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }
} 