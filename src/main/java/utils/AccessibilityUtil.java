package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtil;

import java.time.Duration;
import java.util.List;

public class AccessibilityUtil {
    private WebDriver driver;
    private Actions actions;

    public AccessibilityUtil(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    // Check ARIA labels
    public boolean hasAriaLabel(WebElement element) {
        String ariaLabel = element.getAttribute("aria-label");
        return ariaLabel != null && !ariaLabel.trim().isEmpty();
    }

    public boolean hasAriaLabelledBy(WebElement element) {
        String ariaLabelledBy = element.getAttribute("aria-labelledby");
        return ariaLabelledBy != null && !ariaLabelledBy.trim().isEmpty();
    }

    public boolean hasAriaDescribedBy(WebElement element) {
        String ariaDescribedBy = element.getAttribute("aria-describedby");
        return ariaDescribedBy != null && !ariaDescribedBy.trim().isEmpty();
    }

    // Check keyboard navigation
    public boolean isKeyboardNavigable(WebElement element) {
        try {
            element.sendKeys(Keys.TAB);
            return element.equals(driver.switchTo().activeElement());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canNavigateWithTab() {
        try {
            WebElement firstElement = driver.findElement(By.cssSelector("input, button, a, select, textarea"));
            firstElement.sendKeys(Keys.TAB);
            WebElement secondElement = driver.switchTo().activeElement();
            return !firstElement.equals(secondElement);
        } catch (Exception e) {
            return false;
        }
    }

    // Check focus indicators
    public boolean hasFocusIndicator(WebElement element) {
        element.click();
        String cssValue = element.getCssValue("outline");
        return !cssValue.equals("none") && !cssValue.isEmpty();
    }

    // Check color contrast (basic implementation)
    public boolean hasGoodColorContrast(WebElement element) {
        String backgroundColor = element.getCssValue("background-color");
        String color = element.getCssValue("color");
        // Basic contrast check - in real implementation, you'd use a color contrast library
        return !backgroundColor.equals(color);
    }

    // Check alt text for images
    public boolean hasAltText(WebElement image) {
        String altText = image.getAttribute("alt");
        return altText != null && !altText.trim().isEmpty();
    }

    // Check form labels
    public boolean hasAssociatedLabel(WebElement input) {
        String id = input.getAttribute("id");
        if (id != null && !id.isEmpty()) {
            try {
                WebElement label = driver.findElement(By.cssSelector("label[for='" + id + "']"));
                return label != null;
            } catch (NoSuchElementException e) {
                return false;
            }
        }
        return false;
    }

    // Check heading structure
    public boolean hasProperHeadingStructure() {
        List<WebElement> headings = driver.findElements(By.cssSelector("h1, h2, h3, h4, h5, h6"));
        if (headings.isEmpty()) return false;
        
        int previousLevel = 0;
        for (WebElement heading : headings) {
            String tagName = heading.getTagName();
            int currentLevel = Integer.parseInt(tagName.substring(1));
            
            if (currentLevel - previousLevel > 1) {
                return false; // Skipped heading level
            }
            previousLevel = currentLevel;
        }
        return true;
    }

    // Check skip links
    public boolean hasSkipLinks() {
        try {
            List<WebElement> skipLinks = driver.findElements(By.cssSelector("a[href^='#'], .skip-link"));
            return !skipLinks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Check semantic HTML
    public boolean usesSemanticHTML() {
        List<WebElement> semanticElements = driver.findElements(
            By.cssSelector("nav, main, article, section, aside, header, footer, figure, figcaption")
        );
        return !semanticElements.isEmpty();
    }

    // Comprehensive accessibility audit
    public AccessibilityReport runAccessibilityAudit() {
        AccessibilityReport report = new AccessibilityReport();
        
        // Check all images for alt text
        List<WebElement> images = driver.findElements(By.tagName("img"));
        for (WebElement img : images) {
            if (!hasAltText(img)) {
                report.addIssue("Image missing alt text: " + img.getAttribute("src"));
            }
        }
        
        // Check all inputs for labels
        List<WebElement> inputs = driver.findElements(By.cssSelector("input, select, textarea"));
        for (WebElement input : inputs) {
            if (!hasAssociatedLabel(input)) {
                report.addIssue("Input missing associated label: " + input.getAttribute("id"));
            }
        }
        
        // Check heading structure
        if (!hasProperHeadingStructure()) {
            report.addIssue("Improper heading structure detected");
        }
        
        // Check keyboard navigation
        if (!canNavigateWithTab()) {
            report.addIssue("Keyboard navigation not working properly");
        }
        
        return report;
    }

    // Inner class for accessibility report
    public static class AccessibilityReport {
        private List<String> issues = new java.util.ArrayList<>();
        
        public void addIssue(String issue) {
            issues.add(issue);
        }
        
        public List<String> getIssues() {
            return issues;
        }
        
        public boolean hasIssues() {
            return !issues.isEmpty();
        }
        
        public int getIssueCount() {
            return issues.size();
        }
    }
} 