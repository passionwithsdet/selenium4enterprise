package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {
    private static ExtentReports extentReports;
    
    public static ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = createInstance();
        }
        return extentReports;
    }
    
    private static ExtentReports createInstance() {
        ExtentReports extent = new ExtentReports();
        
        // Create report directory
        String reportPath = ConfigReader.get("extent.report.path", "target/extent-reports/");
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        // Configure Spark Reporter
        String reportFile = reportPath + "index.html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Enterprise Selenium Framework Report");
        sparkReporter.config().setReportName("DemoQA Test Execution Report");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        
        // Add system info
        extent.setSystemInfo("Environment", ConfigReader.getEnvironment());
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
        
        extent.attachReporter(sparkReporter);
        
        LogUtil.info("ExtentReports initialized with file: " + reportFile);
        return extent;
    }
} 