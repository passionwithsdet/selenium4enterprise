package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.DataProviderUtil;

import java.util.Map;

public class DataDrivenTest extends BaseTest {

    @DataProvider(name = "jsonData")
    public Object[][] jsonData() {
        String path = System.getProperty("user.dir") + "/src/main/resources/data/testdata.json";
        return DataProviderUtil.getJsonData(path);
    }

    @Test(dataProvider = "jsonData")
    public void testFormSubmission(Map<String, Object> data) {
        driver.get("https://demoqa.com/text-box");
        driver.findElement(By.id("userName"))
                .sendKeys(data.get("firstName") + " " + data.get("lastName"));
        driver.findElement(By.id("userEmail"))
                .sendKeys(data.get("email").toString());
        driver.findElement(By.id("currentAddress"))
                .sendKeys("123 Main St");
        driver.findElement(By.id("permanentAddress"))
                .sendKeys("456 Elm St");
        driver.findElement(By.id("submit")).click();
        Assert.assertTrue(driver.findElement(By.id("output")).isDisplayed());
    }
} 