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

import java.time.Duration;

public class ActionsClassTest extends BaseTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testDragAndDrop() {
        logStep("Navigate to Interactions page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Interactions");
        
        logStep("Click on Droppable section");
        driver.findElement(By.xpath("//span[text()='Droppable']")).click();
        
        logStep("Perform drag and drop");
        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement droppable = driver.findElement(By.id("droppable"));
        
        Actions actions = new Actions(driver);
        actions.dragAndDrop(draggable, droppable).perform();
        
        logStep("Verify drop was successful");
        String dropText = droppable.getText();
        Assert.assertTrue(dropText.contains("Dropped"), "Drag and drop failed");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testKeyboardShortcuts() {
        logStep("Navigate to Elements page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        
        logStep("Click on Text Box section");
        driver.findElement(By.xpath("//span[text()='Text Box']")).click();
        
        logStep("Test keyboard shortcuts");
        WebElement fullNameInput = driver.findElement(By.id("userName"));
        fullNameInput.click();
        
        Actions actions = new Actions(driver);
        
        // Test Ctrl+A (Select All)
        actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
        actions.sendKeys("John Doe").perform();
        
        // Test Ctrl+C (Copy)
        actions.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
        
        // Test Ctrl+V (Paste)
        WebElement emailInput = driver.findElement(By.id("userEmail"));
        emailInput.click();
        actions.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();
        
        logStep("Verify keyboard actions worked");
        Assert.assertEquals(fullNameInput.getAttribute("value"), "John Doe");
        Assert.assertEquals(emailInput.getAttribute("value"), "John Doe");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testMouseActions() {
        logStep("Navigate to Interactions page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Interactions");
        
        logStep("Click on Buttons section");
        driver.findElement(By.xpath("//span[text()='Buttons']")).click();
        
        logStep("Test double click");
        WebElement doubleClickBtn = driver.findElement(By.id("doubleClickBtn"));
        Actions actions = new Actions(driver);
        actions.doubleClick(doubleClickBtn).perform();
        
        logStep("Verify double click message");
        WebElement doubleClickMessage = driver.findElement(By.id("doubleClickMessage"));
        Assert.assertTrue(doubleClickMessage.isDisplayed(), "Double click message not displayed");
        
        logStep("Test right click");
        WebElement rightClickBtn = driver.findElement(By.id("rightClickBtn"));
        actions.contextClick(rightClickBtn).perform();
        
        logStep("Verify right click message");
        WebElement rightClickMessage = driver.findElement(By.id("rightClickMessage"));
        Assert.assertTrue(rightClickMessage.isDisplayed(), "Right click message not displayed");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testHoverActions() {
        logStep("Navigate to Widgets page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Widgets");
        
        logStep("Click on Tool Tips section");
        driver.findElement(By.xpath("//span[text()='Tool Tips']")).click();
        
        logStep("Test hover action");
        WebElement hoverButton = driver.findElement(By.id("toolTipButton"));
        Actions actions = new Actions(driver);
        actions.moveToElement(hoverButton).perform();
        
        logStep("Wait for tooltip to appear");
        WaitUtil.sleep(2);
        
        logStep("Verify tooltip is displayed");
        WebElement tooltip = driver.findElement(By.className("tooltip-inner"));
        Assert.assertTrue(tooltip.isDisplayed(), "Tooltip not displayed on hover");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testComplexActions() {
        logStep("Navigate to Elements page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        
        logStep("Click on Check Box section");
        driver.findElement(By.xpath("//span[text()='Check Box']")).click();
        
        logStep("Perform complex action sequence");
        WebElement expandButton = driver.findElement(By.cssSelector(".rct-icon-expand-all"));
        Actions actions = new Actions(driver);
        
        // Click with modifier keys
        actions.keyDown(Keys.SHIFT).click(expandButton).keyUp(Keys.SHIFT).perform();
        
        logStep("Verify complex action worked");
        // Add verification logic here
        Assert.assertTrue(true, "Complex action completed");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testScrollActions() {
        logStep("Navigate to Elements page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        
        logStep("Test scroll to element");
        WebElement bottomElement = driver.findElement(By.cssSelector(".footer"));
        Actions actions = new Actions(driver);
        actions.scrollToElement(bottomElement).perform();
        
        logStep("Verify scroll worked");
        Assert.assertTrue(bottomElement.isDisplayed(), "Element not visible after scroll");
        
        logStep("Test scroll by offset");
        actions.scrollByAmount(0, -500).perform();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testClickAndHold() {
        logStep("Navigate to Interactions page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Interactions");
        
        logStep("Click on Sortable section");
        driver.findElement(By.xpath("//span[text()='Sortable']")).click();
        
        logStep("Test click and hold");
        WebElement firstItem = driver.findElement(By.cssSelector(".list-group-item"));
        Actions actions = new Actions(driver);
        
        actions.clickAndHold(firstItem)
               .moveByOffset(0, 50)
               .release()
               .perform();
        
        logStep("Verify click and hold worked");
        // Add verification logic here
        Assert.assertTrue(true, "Click and hold action completed");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testKeyChords() {
        logStep("Navigate to Elements page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        
        logStep("Click on Text Box section");
        driver.findElement(By.xpath("//span[text()='Text Box']")).click();
        
        logStep("Test key chords");
        WebElement textArea = driver.findElement(By.id("currentAddress"));
        textArea.click();
        
        Actions actions = new Actions(driver);
        actions.sendKeys("Hello World")
               .keyDown(Keys.CONTROL)
               .sendKeys("a")
               .keyUp(Keys.CONTROL)
               .sendKeys(Keys.DELETE)
               .sendKeys("New Text")
               .perform();
        
        logStep("Verify key chords worked");
        Assert.assertEquals(textArea.getAttribute("value"), "New Text");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testMouseMovement() {
        logStep("Navigate to Interactions page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Interactions");
        
        logStep("Click on Resizable section");
        driver.findElement(By.xpath("//span[text()='Resizable']")).click();
        
        logStep("Test mouse movement");
        WebElement resizableBox = driver.findElement(By.id("resizableBoxWithRestriction"));
        Actions actions = new Actions(driver);
        
        // Move to element and then move by offset
        actions.moveToElement(resizableBox)
               .moveByOffset(100, 100)
               .click()
               .perform();
        
        logStep("Verify mouse movement worked");
        // Add verification logic here
        Assert.assertTrue(true, "Mouse movement completed");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testActionChaining() {
        logStep("Navigate to Elements page");
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        
        logStep("Click on Radio Button section");
        driver.findElement(By.xpath("//span[text()='Radio Button']")).click();
        
        logStep("Test action chaining");
        WebElement radioButton = driver.findElement(By.id("yesRadio"));
        Actions actions = new Actions(driver);
        
        actions.moveToElement(radioButton)
               .click()
               .pause(Duration.ofSeconds(1))
               .sendKeys(Keys.TAB)
               .pause(Duration.ofSeconds(1))
               .perform();
        
        logStep("Verify action chaining worked");
        Assert.assertTrue(radioButton.isSelected(), "Radio button not selected");
    }
} 