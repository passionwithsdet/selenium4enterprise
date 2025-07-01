package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenShotUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    public static String capture(WebDriver driver, String testName) {
        return capture(driver, testName, "screenshot");
    }
    
    public static String capture(WebDriver driver, String testName, String prefix) {
        try {
            // Create screenshot directory
            String screenshotPath = ConfigReader.get("screenshot.path", "target/screenshots/");
            Path screenshotDir = Paths.get(screenshotPath);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }
            
            // Generate filename
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = String.format("%s_%s_%s.png", prefix, testName, timestamp);
            String fullPath = screenshotPath + fileName;
            
            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshot = ts.getScreenshotAs(OutputType.FILE);
            
            // Move to target location
            File targetFile = new File(fullPath);
            Files.copy(screenshot.toPath(), targetFile.toPath());
            
            LogUtil.logScreenshot(fullPath);
            return fullPath;
            
        } catch (IOException e) {
            LogUtil.error("Failed to capture screenshot", e);
            return "";
        }
    }
    
    public static String captureFullPage(WebDriver driver, String testName) {
        try {
            // Create screenshot directory
            String screenshotPath = ConfigReader.get("screenshot.path", "target/screenshots/");
            Path screenshotDir = Paths.get(screenshotPath);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }
            
            // Generate filename
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = String.format("fullpage_%s_%s.png", testName, timestamp);
            String fullPath = screenshotPath + fileName;
            
            // Take full page screenshot using JavaScript
            String script = "return new Promise((resolve) => {" +
                "const canvas = document.createElement('canvas');" +
                "const context = canvas.getContext('2d');" +
                "const body = document.body;" +
                "const html = document.documentElement;" +
                "const height = Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight);" +
                "const width = Math.max(body.scrollWidth, body.offsetWidth, html.clientWidth, html.scrollWidth, html.offsetWidth);" +
                "canvas.height = height;" +
                "canvas.width = width;" +
                "html2canvas(document.body, {width: width, height: height}).then(canvas => {" +
                "resolve(canvas.toDataURL('image/png'));" +
                "});" +
                "});";
            
            // For now, use regular screenshot as full page screenshot requires additional libraries
            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshot = ts.getScreenshotAs(OutputType.FILE);
            
            // Move to target location
            File targetFile = new File(fullPath);
            Files.copy(screenshot.toPath(), targetFile.toPath());
            
            LogUtil.logScreenshot(fullPath);
            return fullPath;
            
        } catch (IOException e) {
            LogUtil.error("Failed to capture full page screenshot", e);
            return "";
        }
    }
    
    public static String captureElement(WebDriver driver, String testName, String elementSelector) {
        try {
            // Create screenshot directory
            String screenshotPath = ConfigReader.get("screenshot.path", "target/screenshots/");
            Path screenshotDir = Paths.get(screenshotPath);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }
            
            // Generate filename
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = String.format("element_%s_%s_%s.png", testName, elementSelector.replaceAll("[^a-zA-Z0-9]", "_"), timestamp);
            String fullPath = screenshotPath + fileName;
            
            // Take element screenshot using JavaScript
            String script = "var element = document.querySelector('" + elementSelector + "');" +
                "if (element) {" +
                "var canvas = document.createElement('canvas');" +
                "var context = canvas.getContext('2d');" +
                "var rect = element.getBoundingClientRect();" +
                "canvas.width = rect.width;" +
                "canvas.height = rect.height;" +
                "html2canvas(element).then(canvas => {" +
                "return canvas.toDataURL('image/png');" +
                "});" +
                "}";
            
            // For now, use regular screenshot as element screenshot requires additional libraries
            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshot = ts.getScreenshotAs(OutputType.FILE);
            
            // Move to target location
            File targetFile = new File(fullPath);
            Files.copy(screenshot.toPath(), targetFile.toPath());
            
            LogUtil.logScreenshot(fullPath);
            return fullPath;
            
        } catch (IOException e) {
            LogUtil.error("Failed to capture element screenshot", e);
            return "";
        }
    }
}
