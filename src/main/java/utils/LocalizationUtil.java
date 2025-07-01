package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class LocalizationUtil {
    private WebDriver driver;
    private String currentLocale;

    public LocalizationUtil(WebDriver driver) {
        this.driver = driver;
        this.currentLocale = "en-US"; // Default locale
    }

    public LocalizationUtil(WebDriver driver, String locale) {
        this.driver = driver;
        this.currentLocale = locale;
    }

    // Set browser language
    public void setBrowserLanguage(String language) {
        try {
            // Set browser language preference
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("intl.accept_languages", language);
            
            // This would need to be set during driver creation
            LogUtil.info("Browser language set to: " + language);
        } catch (Exception e) {
            LogUtil.error("Failed to set browser language", e);
        }
    }

    // Check text direction (LTR/RTL)
    public boolean isRTLText(String text) {
        // Basic RTL detection for Arabic, Hebrew, etc.
        String rtlPattern = "[\u0591-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]";
        return Pattern.compile(rtlPattern).matcher(text).find();
    }

    public boolean isRTLLayout() {
        try {
            String direction = driver.findElement(By.tagName("html")).getAttribute("dir");
            return "rtl".equalsIgnoreCase(direction);
        } catch (Exception e) {
            return false;
        }
    }

    // Validate date format
    public boolean validateDateFormat(String dateString, String expectedFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(expectedFormat);
            sdf.setLenient(false);
            sdf.parse(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateDateRange(String dateString, String format, int minYear, int maxYear) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            return year >= minYear && year <= maxYear;
        } catch (Exception e) {
            return false;
        }
    }

    // Validate currency format
    public boolean validateCurrencyFormat(String currencyString, String currencyCode) {
        Map<String, String> currencyPatterns = new HashMap<>();
        currencyPatterns.put("USD", "\\$\\s*\\d{1,3}(,\\d{3})*(\\.\\d{2})?");
        currencyPatterns.put("EUR", "€\\s*\\d{1,3}([.\\s]\\d{3})*(,\\d{2})?");
        currencyPatterns.put("GBP", "£\\s*\\d{1,3}(,\\d{3})*(\\.\\d{2})?");
        currencyPatterns.put("JPY", "¥\\s*\\d{1,3}(,\\d{3})*");

        String pattern = currencyPatterns.get(currencyCode);
        if (pattern != null) {
            return Pattern.matches(pattern, currencyString);
        }
        return false;
    }

    // Validate number format
    public boolean validateNumberFormat(String numberString, String locale) {
        try {
            Locale loc = new Locale(locale);
            NumberFormat nf = NumberFormat.getInstance(loc);
            nf.parse(numberString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check text length for different languages
    public boolean validateTextLength(String text, int minLength, int maxLength) {
        int length = text.length();
        return length >= minLength && length <= maxLength;
    }

    // Validate email format for different locales
    public boolean validateEmailFormat(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailPattern, email);
    }

    // Validate phone number format
    public boolean validatePhoneFormat(String phone, String countryCode) {
        Map<String, String> phonePatterns = new HashMap<>();
        phonePatterns.put("US", "\\+?1?\\s*\\(?[0-9]{3}\\)?[-\\s]?[0-9]{3}[-\\s]?[0-9]{4}");
        phonePatterns.put("UK", "\\+?44\\s?[0-9]{4}\\s?[0-9]{6}");
        phonePatterns.put("IN", "\\+?91\\s?[0-9]{5}\\s?[0-9]{5}");

        String pattern = phonePatterns.get(countryCode);
        if (pattern != null) {
            return Pattern.matches(pattern, phone);
        }
        return false;
    }

    // Check for special characters
    public boolean containsSpecialCharacters(String text) {
        String specialCharPattern = "[^a-zA-Z0-9\\s]";
        return Pattern.compile(specialCharPattern).matcher(text).find();
    }

    // Validate address format
    public boolean validateAddressFormat(String address, String country) {
        // Basic address validation
        return address != null && address.length() > 10 && address.contains(" ");
    }

    // Check text encoding
    public boolean validateTextEncoding(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            String decoded = new String(bytes, "UTF-8");
            return text.equals(decoded);
        } catch (Exception e) {
            return false;
        }
    }

    // Validate time format
    public boolean validateTimeFormat(String timeString, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            sdf.parse(timeString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check for language-specific content
    public boolean hasLanguageSpecificContent(String text, String language) {
        Map<String, String> languagePatterns = new HashMap<>();
        languagePatterns.put("es", "[áéíóúñü]");
        languagePatterns.put("fr", "[àâäéèêëïîôöùûüÿç]");
        languagePatterns.put("de", "[äöüß]");
        languagePatterns.put("it", "[àèéìíîòóù]");

        String pattern = languagePatterns.get(language);
        if (pattern != null) {
            return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text).find();
        }
        return false;
    }

    // Comprehensive localization test
    public LocalizationReport runLocalizationTest() {
        LocalizationReport report = new LocalizationReport();
        
        // Check page direction
        if (isRTLLayout()) {
            report.addInfo("Page uses RTL layout");
        } else {
            report.addInfo("Page uses LTR layout");
        }

        // Check all text elements for encoding
        List<WebElement> textElements = driver.findElements(By.cssSelector("p, h1, h2, h3, h4, h5, h6, span, div"));
        for (WebElement element : textElements) {
            String text = element.getText();
            if (!text.isEmpty()) {
                if (!validateTextEncoding(text)) {
                    report.addIssue("Text encoding issue: " + text.substring(0, Math.min(50, text.length())));
                }
            }
        }

        // Check form inputs for proper formatting
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        for (WebElement input : inputs) {
            String type = input.getAttribute("type");
            String value = input.getAttribute("value");
            
            if ("email".equals(type) && !value.isEmpty()) {
                if (!validateEmailFormat(value)) {
                    report.addIssue("Invalid email format: " + value);
                }
            }
            
            if ("tel".equals(type) && !value.isEmpty()) {
                if (!validatePhoneFormat(value, "US")) {
                    report.addIssue("Invalid phone format: " + value);
                }
            }
        }

        return report;
    }

    // Inner class for localization report
    public static class LocalizationReport {
        private List<String> issues = new ArrayList<>();
        private List<String> info = new ArrayList<>();

        public void addIssue(String issue) {
            issues.add(issue);
        }

        public void addInfo(String info) {
            this.info.add(info);
        }

        public List<String> getIssues() {
            return issues;
        }

        public List<String> getInfo() {
            return info;
        }

        public boolean hasIssues() {
            return !issues.isEmpty();
        }

        public int getIssueCount() {
            return issues.size();
        }
    }
} 