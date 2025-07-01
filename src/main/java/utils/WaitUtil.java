package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitUtil {
    
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("page.load.timeout", 30)));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        LogUtil.debug("Page load completed");
    }
    
    public static void waitForElementToBeVisible(WebDriver driver, By locator) {
        waitForElementToBeVisible(driver, locator, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForElementToBeVisible(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        LogUtil.debug("Element visible: " + locator);
    }
    
    public static void waitForElementToBeClickable(WebDriver driver, By locator) {
        waitForElementToBeClickable(driver, locator, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForElementToBeClickable(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        LogUtil.debug("Element clickable: " + locator);
    }
    
    public static void waitForElementToBePresent(WebDriver driver, By locator) {
        waitForElementToBePresent(driver, locator, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForElementToBePresent(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        LogUtil.debug("Element present: " + locator);
    }
    
    public static void waitForElementToDisappear(WebDriver driver, By locator) {
        waitForElementToDisappear(driver, locator, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForElementToDisappear(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        LogUtil.debug("Element disappeared: " + locator);
    }
    
    public static void waitForTextToBePresent(WebDriver driver, By locator, String text) {
        waitForTextToBePresent(driver, locator, text, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForTextToBePresent(WebDriver driver, By locator, String text, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        LogUtil.debug("Text present in element: " + text);
    }
    
    public static void waitForUrlToContain(WebDriver driver, String partialUrl) {
        waitForUrlToContain(driver, partialUrl, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForUrlToContain(WebDriver driver, String partialUrl, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.urlContains(partialUrl));
        LogUtil.debug("URL contains: " + partialUrl);
    }
    
    public static void waitForTitleToContain(WebDriver driver, String partialTitle) {
        waitForTitleToContain(driver, partialTitle, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForTitleToContain(WebDriver driver, String partialTitle, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.titleContains(partialTitle));
        LogUtil.debug("Title contains: " + partialTitle);
    }
    
    public static void waitForAlertToBePresent(WebDriver driver) {
        waitForAlertToBePresent(driver, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForAlertToBePresent(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.alertIsPresent());
        LogUtil.debug("Alert is present");
    }
    
    public static void waitForFrameToBeAvailable(WebDriver driver, By frameLocator) {
        waitForFrameToBeAvailable(driver, frameLocator, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForFrameToBeAvailable(WebDriver driver, By frameLocator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
        LogUtil.debug("Frame available and switched: " + frameLocator);
    }
    
    public static void waitForNumberOfElements(WebDriver driver, By locator, int expectedCount) {
        waitForNumberOfElements(driver, locator, expectedCount, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForNumberOfElements(WebDriver driver, By locator, int expectedCount, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
        LogUtil.debug("Number of elements: " + expectedCount);
    }
    
    public static void waitForElementToHaveAttribute(WebDriver driver, By locator, String attribute, String value) {
        waitForElementToHaveAttribute(driver, locator, attribute, value, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForElementToHaveAttribute(WebDriver driver, By locator, String attribute, String value, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
        LogUtil.debug("Element has attribute: " + attribute + " = " + value);
    }
    
    public static void waitForElementToBeSelected(WebDriver driver, By locator) {
        waitForElementToBeSelected(driver, locator, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForElementToBeSelected(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.elementToBeSelected(locator));
        LogUtil.debug("Element selected: " + locator);
    }
    
    public static void waitForStalenessOf(WebDriver driver, WebElement element) {
        waitForStalenessOf(driver, element, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForStalenessOf(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.stalenessOf(element));
        LogUtil.debug("Element became stale");
    }
    
    public static void waitForJQueryToLoad(WebDriver driver) {
        waitForJQueryToLoad(driver, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForJQueryToLoad(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until((ExpectedCondition<Boolean>) webDriver -> {
            try {
                return (Boolean) ((JavascriptExecutor) webDriver)
                        .executeScript("return jQuery.active == 0");
            } catch (Exception e) {
                return true; // jQuery not available
            }
        });
        LogUtil.debug("jQuery loaded");
    }
    
    public static void waitForAngularToLoad(WebDriver driver) {
        waitForAngularToLoad(driver, ConfigReader.getInt("explicit.wait", 20));
    }
    
    public static void waitForAngularToLoad(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until((ExpectedCondition<Boolean>) webDriver -> {
            try {
                return (Boolean) ((JavascriptExecutor) webDriver)
                        .executeScript("return angular.element(document).injector().get('$http').pendingRequests.length === 0");
            } catch (Exception e) {
                return true; // Angular not available
            }
        });
        LogUtil.debug("Angular loaded");
    }
    
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            LogUtil.debug("Slept for " + seconds + " seconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogUtil.error("Sleep interrupted", e);
        }
    }
    
    public static void sleepMillis(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
            LogUtil.debug("Slept for " + milliseconds + " milliseconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogUtil.error("Sleep interrupted", e);
        }
    }
} 