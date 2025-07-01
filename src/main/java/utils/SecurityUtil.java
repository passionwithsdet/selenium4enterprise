package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SecurityUtil {
    private static final String SECURITY_LOG_PATH = "target/security-results/";
    private static final List<String> securityIssues = new ArrayList<>();
    
    // Common SQL injection patterns
    private static final String[] SQL_INJECTION_PATTERNS = {
        "' OR '1'='1",
        "'; DROP TABLE users; --",
        "' UNION SELECT * FROM users --",
        "admin'--",
        "1' OR '1' = '1' --",
        "1; DROP TABLE users --"
    };
    
    // Common XSS patterns
    private static final String[] XSS_PATTERNS = {
        "<script>alert('XSS')</script>",
        "<img src=x onerror=alert('XSS')>",
        "javascript:alert('XSS')",
        "<svg onload=alert('XSS')>",
        "';alert('XSS');//"
    };
    
    // Common path traversal patterns
    private static final String[] PATH_TRAVERSAL_PATTERNS = {
        "../../../etc/passwd",
        "..\\..\\..\\windows\\system32\\config\\sam",
        "....//....//....//etc/passwd",
        "..%2F..%2F..%2Fetc%2Fpasswd"
    };
    
    static {
        createSecurityDirectory();
    }

    private static void createSecurityDirectory() {
        try {
            Path path = Paths.get(SECURITY_LOG_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            LogUtil.error("Failed to create security directory", e);
        }
    }

    /**
     * Test for SQL injection vulnerabilities
     */
    public static boolean testSqlInjection(WebDriver driver, String url, String inputFieldId) {
        LogUtil.info("Testing SQL injection on: " + url);
        
        boolean vulnerabilityFound = false;
        
        try {
            driver.get(url);
            
            for (String payload : SQL_INJECTION_PATTERNS) {
                try {
                    WebElement inputField = driver.findElement(org.openqa.selenium.By.id(inputFieldId));
                    inputField.clear();
                    inputField.sendKeys(payload);
                    
                    // Submit the form
                    WebElement submitButton = driver.findElement(org.openqa.selenium.By.cssSelector("input[type='submit'], button[type='submit']"));
                    submitButton.click();
                    
                    // Check for SQL error messages
                    String pageSource = driver.getPageSource().toLowerCase();
                    if (containsSqlError(pageSource)) {
                        LogUtil.warn("SQL Injection vulnerability found with payload: " + payload);
                        securityIssues.add("SQL Injection: " + payload + " at " + url);
                        vulnerabilityFound = true;
                    }
                    
                    // Wait before next test
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    LogUtil.error("Error testing SQL injection payload: " + payload, e);
                }
            }
            
        } catch (Exception e) {
            LogUtil.error("Failed to test SQL injection", e);
        }
        
        return vulnerabilityFound;
    }

    /**
     * Test for XSS vulnerabilities
     */
    public static boolean testXss(WebDriver driver, String url, String inputFieldId) {
        LogUtil.info("Testing XSS on: " + url);
        
        boolean vulnerabilityFound = false;
        
        try {
            driver.get(url);
            
            for (String payload : XSS_PATTERNS) {
                try {
                    WebElement inputField = driver.findElement(org.openqa.selenium.By.id(inputFieldId));
                    inputField.clear();
                    inputField.sendKeys(payload);
                    
                    // Submit the form
                    WebElement submitButton = driver.findElement(org.openqa.selenium.By.cssSelector("input[type='submit'], button[type='submit']"));
                    submitButton.click();
                    
                    // Check if XSS payload is reflected in the page
                    String pageSource = driver.getPageSource();
                    if (pageSource.contains(payload)) {
                        LogUtil.warn("XSS vulnerability found with payload: " + payload);
                        securityIssues.add("XSS: " + payload + " at " + url);
                        vulnerabilityFound = true;
                    }
                    
                    // Wait before next test
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    LogUtil.error("Error testing XSS payload: " + payload, e);
                }
            }
            
        } catch (Exception e) {
            LogUtil.error("Failed to test XSS", e);
        }
        
        return vulnerabilityFound;
    }

    /**
     * Test for path traversal vulnerabilities
     */
    public static boolean testPathTraversal(WebDriver driver, String url) {
        LogUtil.info("Testing path traversal on: " + url);
        
        boolean vulnerabilityFound = false;
        
        try {
            for (String payload : PATH_TRAVERSAL_PATTERNS) {
                try {
                    String testUrl = url + payload;
                    driver.get(testUrl);
                    
                    // Check for sensitive file content
                    String pageSource = driver.getPageSource();
                    if (containsSensitiveData(pageSource)) {
                        LogUtil.warn("Path traversal vulnerability found with payload: " + payload);
                        securityIssues.add("Path Traversal: " + payload + " at " + url);
                        vulnerabilityFound = true;
                    }
                    
                    // Wait before next test
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    LogUtil.error("Error testing path traversal payload: " + payload, e);
                }
            }
            
        } catch (Exception e) {
            LogUtil.error("Failed to test path traversal", e);
        }
        
        return vulnerabilityFound;
    }

    /**
     * Test for CSRF vulnerabilities
     */
    public static boolean testCsrf(WebDriver driver, String url) {
        LogUtil.info("Testing CSRF on: " + url);
        
        boolean vulnerabilityFound = false;
        
        try {
            // Get the page source to check for CSRF tokens
            driver.get(url);
            String pageSource = driver.getPageSource();
            
            // Check if CSRF token is present
            if (!containsCsrfToken(pageSource)) {
                LogUtil.warn("CSRF protection might be missing on: " + url);
                securityIssues.add("CSRF: Missing CSRF token at " + url);
                vulnerabilityFound = true;
            }
            
        } catch (Exception e) {
            LogUtil.error("Failed to test CSRF", e);
        }
        
        return vulnerabilityFound;
    }

    /**
     * Test for sensitive data exposure
     */
    public static boolean testSensitiveDataExposure(WebDriver driver, String url) {
        LogUtil.info("Testing sensitive data exposure on: " + url);
        
        boolean vulnerabilityFound = false;
        
        try {
            driver.get(url);
            String pageSource = driver.getPageSource();
            
            // Check for common sensitive data patterns
            if (containsSensitiveData(pageSource)) {
                LogUtil.warn("Sensitive data exposure found on: " + url);
                securityIssues.add("Sensitive Data Exposure: Found sensitive data at " + url);
                vulnerabilityFound = true;
            }
            
        } catch (Exception e) {
            LogUtil.error("Failed to test sensitive data exposure", e);
        }
        
        return vulnerabilityFound;
    }

    /**
     * Test for input validation
     */
    public static boolean testInputValidation(WebDriver driver, String url, String inputFieldId) {
        LogUtil.info("Testing input validation on: " + url);
        
        boolean vulnerabilityFound = false;
        
        try {
            driver.get(url);
            
            // Test with various malicious inputs
            String[] maliciousInputs = {
                "<script>alert('test')</script>",
                "'; DROP TABLE users; --",
                "../../../etc/passwd",
                "javascript:alert('test')",
                "admin' OR '1'='1"
            };
            
            for (String input : maliciousInputs) {
                try {
                    WebElement inputField = driver.findElement(org.openqa.selenium.By.id(inputFieldId));
                    inputField.clear();
                    inputField.sendKeys(input);
                    
                    // Submit the form
                    WebElement submitButton = driver.findElement(org.openqa.selenium.By.cssSelector("input[type='submit'], button[type='submit']"));
                    submitButton.click();
                    
                    // Check if input was properly sanitized
                    String pageSource = driver.getPageSource();
                    if (pageSource.contains(input)) {
                        LogUtil.warn("Input validation issue found with input: " + input);
                        securityIssues.add("Input Validation: " + input + " at " + url);
                        vulnerabilityFound = true;
                    }
                    
                    // Wait before next test
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    LogUtil.error("Error testing input validation: " + input, e);
                }
            }
            
        } catch (Exception e) {
            LogUtil.error("Failed to test input validation", e);
        }
        
        return vulnerabilityFound;
    }

    /**
     * Generate security report
     */
    public static void generateSecurityReport() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = SECURITY_LOG_PATH + "security-report-" + timestamp + ".html";
            
            StringBuilder report = new StringBuilder();
            report.append("<!DOCTYPE html>\n");
            report.append("<html><head><title>Security Test Report</title></head><body>\n");
            report.append("<h1>Security Test Report</h1>\n");
            report.append("<p>Generated: " + LocalDateTime.now() + "</p>\n");
            report.append("<h2>Security Issues Found: " + securityIssues.size() + "</h2>\n");
            
            if (!securityIssues.isEmpty()) {
                report.append("<ul>\n");
                for (String issue : securityIssues) {
                    report.append("<li>").append(issue).append("</li>\n");
                }
                report.append("</ul>\n");
            } else {
                report.append("<p>No security issues found.</p>\n");
            }
            
            report.append("</body></html>");
            
            try (FileWriter writer = new FileWriter(reportPath)) {
                writer.write(report.toString());
            }
            
            LogUtil.info("Security report generated: " + reportPath);
            
        } catch (IOException e) {
            LogUtil.error("Failed to generate security report", e);
        }
    }

    /**
     * Clear security issues
     */
    public static void clearSecurityIssues() {
        securityIssues.clear();
        LogUtil.info("Security issues cleared");
    }

    /**
     * Get security issues
     */
    public static List<String> getSecurityIssues() {
        return new ArrayList<>(securityIssues);
    }

    private static boolean containsSqlError(String pageSource) {
        String[] sqlErrors = {
            "sql syntax",
            "mysql error",
            "oracle error",
            "sql server error",
            "postgresql error",
            "database error",
            "sql exception"
        };
        
        for (String error : sqlErrors) {
            if (pageSource.contains(error)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsSensitiveData(String pageSource) {
        String[] sensitivePatterns = {
            "password",
            "credit card",
            "ssn",
            "social security",
            "api key",
            "secret",
            "token"
        };
        
        for (String pattern : sensitivePatterns) {
            if (pageSource.toLowerCase().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsCsrfToken(String pageSource) {
        String[] csrfPatterns = {
            "csrf",
            "_token",
            "authenticity_token",
            "xsrf"
        };
        
        for (String pattern : csrfPatterns) {
            if (pageSource.toLowerCase().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Validate password strength
     */
    public static boolean isStrongPassword(String password) {
        // At least 8 characters, 1 uppercase, 1 lowercase, 1 number, 1 special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }
} 