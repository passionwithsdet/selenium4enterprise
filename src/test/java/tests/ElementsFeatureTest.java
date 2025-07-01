package tests;

import base.BaseTest;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ElementsPage;
import pages.HomePage;
import utils.WaitUtil;
import listeners.RetryAnalyzer;

public class ElementsFeatureTest extends BaseTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testAlerts() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Alerts, Frame & Windows");
        ElementsPage elements = new ElementsPage(driver);
        elements.clickAlertButton();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "You clicked a button");
        alert.accept();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testPopups() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Modal Dialogs");
        ElementsPage elements = new ElementsPage(driver);
        Assert.assertTrue(elements.isModalDisplayed());
        elements.closeModal();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testMultipleWindowsTabs() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Alerts, Frame & Windows");
        ElementsPage elements = new ElementsPage(driver);
        elements.openNewTab();
        elements.switchToNewWindow();
        Assert.assertTrue(driver.getCurrentUrl().contains("demoqa.com"));
        elements.switchToMainWindow();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testActions() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Interactions");
        ElementsPage elements = new ElementsPage(driver);
        elements.dragAndDrop();
        // Add assertion for droppable state if needed
        elements.rightClick();
        elements.doubleClick();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testDropDownSelect() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Widgets");
        ElementsPage elements = new ElementsPage(driver);
        elements.selectByVisibleText("Purple");
        elements.selectByValue("4");
        elements.selectByIndex(2);
        // Add assertion for selected value if needed
    }
} 