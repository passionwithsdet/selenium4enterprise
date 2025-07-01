package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VisualTestingUtil {
    private WebDriver driver;
    private static final String BASELINE_DIR = "src/test/resources/baseline/";
    private static final String ACTUAL_DIR = "target/visual-tests/actual/";
    private static final String DIFF_DIR = "target/visual-tests/diff/";
    private static final double SIMILARITY_THRESHOLD = 0.95; // 95% similarity

    public VisualTestingUtil(WebDriver driver) {
        this.driver = driver;
        createDirectories();
    }

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(BASELINE_DIR));
            Files.createDirectories(Paths.get(ACTUAL_DIR));
            Files.createDirectories(Paths.get(DIFF_DIR));
        } catch (IOException e) {
            LogUtil.error("Failed to create visual testing directories", e);
        }
    }

    // Capture full page screenshot
    public String captureFullPageScreenshot(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshot = ts.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = ACTUAL_DIR + fileName;
            
            File targetFile = new File(filePath);
            Files.copy(screenshot.toPath(), targetFile.toPath());
            
            LogUtil.info("Full page screenshot captured: " + filePath);
            return filePath;
        } catch (IOException e) {
            LogUtil.error("Failed to capture full page screenshot", e);
            return "";
        }
    }

    // Capture element screenshot
    public String captureElementScreenshot(WebElement element, String testName) {
        try {
            File screenshot = element.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_element_" + timestamp + ".png";
            String filePath = ACTUAL_DIR + fileName;
            
            File targetFile = new File(filePath);
            Files.copy(screenshot.toPath(), targetFile.toPath());
            
            LogUtil.info("Element screenshot captured: " + filePath);
            return filePath;
        } catch (IOException e) {
            LogUtil.error("Failed to capture element screenshot", e);
            return "";
        }
    }

    // Compare screenshots
    public boolean compareScreenshots(String baselinePath, String actualPath) {
        try {
            BufferedImage baseline = ImageIO.read(new File(baselinePath));
            BufferedImage actual = ImageIO.read(new File(actualPath));
            
            if (baseline.getWidth() != actual.getWidth() || baseline.getHeight() != actual.getHeight()) {
                LogUtil.warn("Screenshot dimensions don't match");
                return false;
            }
            
            int width = baseline.getWidth();
            int height = baseline.getHeight();
            int totalPixels = width * height;
            int differentPixels = 0;
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (baseline.getRGB(x, y) != actual.getRGB(x, y)) {
                        differentPixels++;
                    }
                }
            }
            
            double similarity = 1.0 - ((double) differentPixels / totalPixels);
            LogUtil.info("Screenshot similarity: " + (similarity * 100) + "%");
            
            if (similarity < SIMILARITY_THRESHOLD) {
                createDifferenceImage(baseline, actual, actualPath);
            }
            
            return similarity >= SIMILARITY_THRESHOLD;
        } catch (IOException e) {
            LogUtil.error("Failed to compare screenshots", e);
            return false;
        }
    }

    // Create difference image
    private void createDifferenceImage(BufferedImage baseline, BufferedImage actual, String actualPath) {
        try {
            BufferedImage diff = new BufferedImage(baseline.getWidth(), baseline.getHeight(), BufferedImage.TYPE_INT_RGB);
            
            for (int y = 0; y < baseline.getHeight(); y++) {
                for (int x = 0; x < baseline.getWidth(); x++) {
                    int baselineRGB = baseline.getRGB(x, y);
                    int actualRGB = actual.getRGB(x, y);
                    
                    if (baselineRGB != actualRGB) {
                        diff.setRGB(x, y, Color.RED.getRGB()); // Highlight differences in red
                    } else {
                        diff.setRGB(x, y, baselineRGB);
                    }
                }
            }
            
            String diffPath = actualPath.replace(ACTUAL_DIR, DIFF_DIR).replace(".png", "_diff.png");
            ImageIO.write(diff, "PNG", new File(diffPath));
            LogUtil.info("Difference image created: " + diffPath);
        } catch (IOException e) {
            LogUtil.error("Failed to create difference image", e);
        }
    }

    // Visual regression test
    public boolean runVisualRegressionTest(String testName) {
        String baselinePath = BASELINE_DIR + testName + ".png";
        String actualPath = captureFullPageScreenshot(testName);
        
        if (actualPath.isEmpty()) {
            return false;
        }
        
        File baselineFile = new File(baselinePath);
        if (!baselineFile.exists()) {
            // Create baseline if it doesn't exist
            try {
                Files.copy(Paths.get(actualPath), baselineFile.toPath());
                LogUtil.info("Baseline created: " + baselinePath);
                return true;
            } catch (IOException e) {
                LogUtil.error("Failed to create baseline", e);
                return false;
            }
        }
        
        return compareScreenshots(baselinePath, actualPath);
    }

    // Check element visibility
    public boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed() && element.getSize().getHeight() > 0 && element.getSize().getWidth() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Check element color
    public Color getElementColor(WebElement element) {
        try {
            String colorValue = element.getCssValue("color");
            return Color.decode(colorValue.replace("rgb(", "").replace(")", ""));
        } catch (Exception e) {
            LogUtil.error("Failed to get element color", e);
            return Color.BLACK;
        }
    }

    // Check element size
    public Dimension getElementSize(WebElement element) {
        try {
            org.openqa.selenium.Dimension size = element.getSize();
            return new Dimension(size.getWidth(), size.getHeight());
        } catch (Exception e) {
            LogUtil.error("Failed to get element size", e);
            return new Dimension(0, 0);
        }
    }

    // Check element position
    public Point getElementPosition(WebElement element) {
        try {
            org.openqa.selenium.Point location = element.getLocation();
            return new Point(location.getX(), location.getY());
        } catch (Exception e) {
            LogUtil.error("Failed to get element position", e);
            return new Point(0, 0);
        }
    }

    // Validate UI consistency
    public boolean validateUIConsistency() {
        try {
            // Check if all buttons have consistent styling
            java.util.List<WebElement> buttons = driver.findElements(By.tagName("button"));
            if (buttons.size() > 1) {
                String firstButtonColor = buttons.get(0).getCssValue("background-color");
                for (WebElement button : buttons) {
                    if (!button.getCssValue("background-color").equals(firstButtonColor)) {
                        LogUtil.warn("Inconsistent button styling detected");
                        return false;
                    }
                }
            }
            
            // Check if all inputs have consistent styling
            java.util.List<WebElement> inputs = driver.findElements(By.tagName("input"));
            if (inputs.size() > 1) {
                String firstInputBorder = inputs.get(0).getCssValue("border");
                for (WebElement input : inputs) {
                    if (!input.getCssValue("border").equals(firstInputBorder)) {
                        LogUtil.warn("Inconsistent input styling detected");
                        return false;
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            LogUtil.error("Failed to validate UI consistency", e);
            return false;
        }
    }

    // Check responsive design
    public boolean checkResponsiveDesign() {
        try {
            // Test different viewport sizes
            Dimension[] viewports = {
                new Dimension(1920, 1080), // Desktop
                new Dimension(1024, 768),  // Tablet
                new Dimension(375, 812)    // Mobile
            };
            
            for (Dimension viewport : viewports) {
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(viewport.width, viewport.height));
                WaitUtil.sleep(1); // Wait for layout to adjust
                
                // Check if page is still functional
                if (!driver.findElement(By.tagName("body")).isDisplayed()) {
                    LogUtil.warn("Page not responsive at " + viewport.width + "x" + viewport.height);
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            LogUtil.error("Failed to check responsive design", e);
            return false;
        }
    }
} 