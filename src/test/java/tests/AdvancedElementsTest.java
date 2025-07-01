package tests;

import base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.WaitUtil;
import listeners.RetryAnalyzer;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class AdvancedElementsTest extends BaseTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testFileUpload() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        driver.findElement(By.xpath("//span[text()='Upload and Download']")).click();
        String filePath = System.getProperty("user.dir") + "/src/test/resources/data/sample-upload.txt";
        driver.findElement(By.id("uploadFile")).sendKeys(filePath);
        String uploadedPath = driver.findElement(By.id("uploadedFilePath")).getText();
        Assert.assertTrue(uploadedPath.contains("sample-upload.txt"));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testFileDownload() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        driver.findElement(By.xpath("//span[text()='Upload and Download']")).click();
        driver.findElement(By.id("downloadButton")).click();
        // Add assertion for file existence if needed (requires custom download dir setup)
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testJavaScriptExecutor() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)");
        String title = (String) js.executeScript("return document.title;");
        Assert.assertTrue(title.contains("DEMOQA"));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testKeyboardMouseSimulation() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        Actions actions = new Actions(driver);
        WebElement searchBox = driver.findElement(By.tagName("body"));
        actions.moveToElement(searchBox).click().sendKeys(Keys.TAB).sendKeys("Test").perform();
        // No assertion, just demo
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testDynamicWaits() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Dynamic Properties");
        By enableAfter = By.id("enableAfter");
        WaitUtil.waitForElementToBeClickable(driver, enableAfter, 10);
        Assert.assertTrue(driver.findElement(enableAfter).isEnabled());
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testFrameHandling() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Alerts, Frame & Windows");
        driver.findElement(By.xpath("//span[text()='Frames']")).click();
        driver.switchTo().frame("frame1");
        String frameText = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(frameText.contains("This is a sample page"));
        driver.switchTo().defaultContent();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testTableOperations() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        driver.findElement(By.xpath("//span[text()='Web Tables']")).click();
        List<WebElement> rows = driver.findElements(By.cssSelector(".rt-tbody .rt-tr-group"));
        Assert.assertTrue(rows.size() > 0);
        // Example: Find a cell value
        boolean found = false;
        for (WebElement row : rows) {
            if (row.getText().contains("Cierra")) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Row with 'Cierra' found");
    }
} 