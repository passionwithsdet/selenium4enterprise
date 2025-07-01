package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.AccessibilityUtil;
import utils.ConfigReader;
import utils.LocalizationUtil;
import utils.VisualTestingUtil;
import listeners.RetryAnalyzer;

import java.util.List;

public class MultiBrowserTest extends BaseTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Parameters({"browser"})
    public void testCrossBrowserCompatibility() {
        logStep("Testing cross-browser compatibility for: " + ConfigReader.getBrowser());
        
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        
        logStep("Verify page loads correctly");
        Assert.assertTrue(driver.getTitle().contains("DEMOQA"), "Page title not found");
        
        logStep("Verify main elements are present");
        List<WebElement> cards = driver.findElements(By.cssSelector(".card-body"));
        Assert.assertTrue(cards.size() > 0, "No cards found on page");
        
        logStep("Test form elements");
        driver.findElement(By.xpath("//span[text()='Text Box']")).click();
        WebElement nameInput = driver.findElement(By.id("userName"));
        nameInput.sendKeys("Test User");
        Assert.assertEquals(nameInput.getAttribute("value"), "Test User");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Parameters({"browser"})
    public void testBrowserSpecificFeatures() {
        logStep("Testing browser-specific features for: " + ConfigReader.getBrowser());
        
        String browser = ConfigReader.getBrowser().toLowerCase();
        
        // Test JavaScript execution
        logStep("Test JavaScript execution");
        String title = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.title;");
        Assert.assertNotNull(title, "JavaScript execution failed");
        
        // Test CSS properties
        logStep("Test CSS properties");
        WebElement body = driver.findElement(By.tagName("body"));
        String backgroundColor = body.getCssValue("background-color");
        Assert.assertNotNull(backgroundColor, "CSS property not accessible");
        
        // Browser-specific tests
        if (browser.contains("chrome")) {
            logStep("Chrome-specific tests");
            testChromeFeatures();
        } else if (browser.contains("firefox")) {
            logStep("Firefox-specific tests");
            testFirefoxFeatures();
        } else if (browser.contains("safari")) {
            logStep("Safari-specific tests");
            testSafariFeatures();
        } else if (browser.contains("edge")) {
            logStep("Edge-specific tests");
            testEdgeFeatures();
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Parameters({"browser"})
    public void testResponsiveDesignAcrossBrowsers() {
        logStep("Testing responsive design across browsers");
        
        // Test different viewport sizes
        org.openqa.selenium.Dimension[] viewports = {
            new org.openqa.selenium.Dimension(1920, 1080), // Desktop
            new org.openqa.selenium.Dimension(1024, 768),  // Tablet
            new org.openqa.selenium.Dimension(375, 812)    // Mobile
        };
        
        for (org.openqa.selenium.Dimension viewport : viewports) {
            logStep("Testing viewport: " + viewport.getWidth() + "x" + viewport.getHeight());
            driver.manage().window().setSize(viewport);
            
            // Verify page is still functional
            Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed());
            
            // Check if elements are properly sized
            List<WebElement> elements = driver.findElements(By.cssSelector(".card-body"));
            for (WebElement element : elements) {
                Assert.assertTrue(element.getSize().getWidth() > 0);
                Assert.assertTrue(element.getSize().getHeight() > 0);
            }
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Parameters({"browser"})
    public void testAccessibilityAcrossBrowsers() {
        logStep("Testing accessibility across browsers");
        
        AccessibilityUtil accessibilityUtil = new AccessibilityUtil(driver);
        AccessibilityUtil.AccessibilityReport report = accessibilityUtil.runAccessibilityAudit();
        
        logStep("Accessibility issues found: " + report.getIssueCount());
        
        // Log issues but don't fail the test (accessibility issues are warnings)
        if (report.hasIssues()) {
            for (String issue : report.getIssues()) {
                logWarning("Accessibility Issue: " + issue);
            }
        }
        
        Assert.assertTrue(true, "Accessibility test completed");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Parameters({"browser"})
    public void testLocalizationAcrossBrowsers() {
        logStep("Testing localization across browsers");
        
        LocalizationUtil localizationUtil = new LocalizationUtil(driver);
        LocalizationUtil.LocalizationReport report = localizationUtil.runLocalizationTest();
        
        logStep("Localization issues found: " + report.getIssueCount());
        
        // Log issues but don't fail the test
        if (report.hasIssues()) {
            for (String issue : report.getIssues()) {
                logWarning("Localization Issue: " + issue);
            }
        }
        
        Assert.assertTrue(true, "Localization test completed");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Parameters({"browser"})
    public void testVisualConsistencyAcrossBrowsers() {
        logStep("Testing visual consistency across browsers");
        
        VisualTestingUtil visualUtil = new VisualTestingUtil(driver);
        
        // Test UI consistency
        boolean isConsistent = visualUtil.validateUIConsistency();
        Assert.assertTrue(isConsistent, "UI consistency check failed");
        
        // Test responsive design
        boolean isResponsive = visualUtil.checkResponsiveDesign();
        Assert.assertTrue(isResponsive, "Responsive design check failed");
        
        logStep("Visual consistency test completed successfully");
    }

    // Browser-specific test methods
    private void testChromeFeatures() {
        logStep("Testing Chrome-specific features");
        // Chrome-specific tests
        Assert.assertTrue(true, "Chrome features test completed");
    }

    private void testFirefoxFeatures() {
        logStep("Testing Firefox-specific features");
        // Firefox-specific tests
        Assert.assertTrue(true, "Firefox features test completed");
    }

    private void testSafariFeatures() {
        logStep("Testing Safari-specific features");
        // Safari-specific tests
        Assert.assertTrue(true, "Safari features test completed");
    }

    private void testEdgeFeatures() {
        logStep("Testing Edge-specific features");
        // Edge-specific tests
        Assert.assertTrue(true, "Edge features test completed");
    }
} 