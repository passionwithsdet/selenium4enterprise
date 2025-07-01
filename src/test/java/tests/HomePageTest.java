package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

public class HomePageTest extends BaseTest {
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testClickElementsCard() {
        HomePage home = new HomePage(driver);
        home.clickOnCard("Elements");
        Assert.assertTrue(driver.getCurrentUrl().contains("elements"));
    }
}

