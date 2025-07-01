package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerformanceUtil {
    private static final Map<String, Long> startTimes = new ConcurrentHashMap<>();
    private static final Map<String, Long> endTimes = new ConcurrentHashMap<>();
    private static final Map<String, Long> memoryUsage = new ConcurrentHashMap<>();
    private static final String PERFORMANCE_LOG_PATH = "target/performance-results/";
    
    static {
        createPerformanceDirectory();
    }

    private static void createPerformanceDirectory() {
        try {
            Path path = Paths.get(PERFORMANCE_LOG_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            LogUtil.error("Failed to create performance directory", e);
        }
    }

    /**
     * Start performance measurement
     */
    public static void startMeasurement(String operationName) {
        long startTime = System.currentTimeMillis();
        startTimes.put(operationName, startTime);
        
        // Record memory usage at start
        Runtime runtime = Runtime.getRuntime();
        long memoryAtStart = runtime.totalMemory() - runtime.freeMemory();
        memoryUsage.put(operationName + "_start", memoryAtStart);
        
        LogUtil.info("Performance measurement started for: " + operationName);
    }

    /**
     * End performance measurement and log results
     */
    public static void endMeasurement(String operationName) {
        long endTime = System.currentTimeMillis();
        endTimes.put(operationName, endTime);
        
        // Calculate duration
        Long startTime = startTimes.get(operationName);
        if (startTime != null) {
            long duration = endTime - startTime;
            
            // Record memory usage at end
            Runtime runtime = Runtime.getRuntime();
            long memoryAtEnd = runtime.totalMemory() - runtime.freeMemory();
            memoryUsage.put(operationName + "_end", memoryAtEnd);
            
            // Calculate memory difference
            Long memoryAtStart = memoryUsage.get(operationName + "_start");
            long memoryDiff = memoryAtStart != null ? memoryAtEnd - memoryAtStart : 0;
            
            // Log performance metrics
            logPerformanceMetrics(operationName, duration, memoryAtEnd, memoryDiff);
            
            // Save to file
            savePerformanceData(operationName, duration, memoryAtEnd, memoryDiff);
        }
    }

    /**
     * Measure page load performance
     */
    public static void measurePageLoad(WebDriver driver, String pageName) {
        startMeasurement("PageLoad_" + pageName);
        
        try {
            // Wait for page to load completely
            WaitUtil.waitForPageLoad(driver);
            
            // Get page load time from browser
            List<LogEntry> logs = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
            long pageLoadTime = extractPageLoadTime(logs);
            
            endMeasurement("PageLoad_" + pageName);
            
            LogUtil.info("Page load time for " + pageName + ": " + pageLoadTime + "ms");
            
        } catch (Exception e) {
            LogUtil.error("Failed to measure page load performance", e);
            endMeasurement("PageLoad_" + pageName);
        }
    }

    /**
     * Measure element interaction performance
     */
    public static void measureElementInteraction(String elementName, Runnable interaction) {
        startMeasurement("ElementInteraction_" + elementName);
        
        try {
            interaction.run();
            endMeasurement("ElementInteraction_" + elementName);
        } catch (Exception e) {
            LogUtil.error("Failed to measure element interaction performance", e);
            endMeasurement("ElementInteraction_" + elementName);
        }
    }

    /**
     * Measure API call performance
     */
    public static void measureApiCall(String apiName, Runnable apiCall) {
        startMeasurement("ApiCall_" + apiName);
        
        try {
            apiCall.run();
            endMeasurement("ApiCall_" + apiName);
        } catch (Exception e) {
            LogUtil.error("Failed to measure API call performance", e);
            endMeasurement("ApiCall_" + apiName);
        }
    }

    /**
     * Get current memory usage
     */
    public static Map<String, Long> getCurrentMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        Map<String, Long> memoryInfo = new HashMap<>();
        
        memoryInfo.put("totalMemory", runtime.totalMemory());
        memoryInfo.put("freeMemory", runtime.freeMemory());
        memoryInfo.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        memoryInfo.put("maxMemory", runtime.maxMemory());
        
        return memoryInfo;
    }

    /**
     * Generate performance report
     */
    public static void generatePerformanceReport() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = PERFORMANCE_LOG_PATH + "performance-report-" + timestamp + ".html";
            
            StringBuilder report = new StringBuilder();
            report.append("<!DOCTYPE html>\n");
            report.append("<html><head><title>Performance Report</title></head><body>\n");
            report.append("<h1>Performance Test Report</h1>\n");
            report.append("<p>Generated: " + LocalDateTime.now() + "</p>\n");
            report.append("<table border='1'>\n");
            report.append("<tr><th>Operation</th><th>Duration (ms)</th><th>Memory Usage (bytes)</th></tr>\n");
            
            for (String operation : startTimes.keySet()) {
                Long startTime = startTimes.get(operation);
                Long endTime = endTimes.get(operation);
                
                if (startTime != null && endTime != null) {
                    long duration = endTime - startTime;
                    Long memoryEnd = memoryUsage.get(operation + "_end");
                    
                    report.append("<tr>");
                    report.append("<td>").append(operation).append("</td>");
                    report.append("<td>").append(duration).append("</td>");
                    report.append("<td>").append(memoryEnd != null ? memoryEnd : "N/A").append("</td>");
                    report.append("</tr>\n");
                }
            }
            
            report.append("</table>\n");
            report.append("</body></html>");
            
            try (FileWriter writer = new FileWriter(reportPath)) {
                writer.write(report.toString());
            }
            
            LogUtil.info("Performance report generated: " + reportPath);
            
        } catch (IOException e) {
            LogUtil.error("Failed to generate performance report", e);
        }
    }

    private static void logPerformanceMetrics(String operationName, long duration, long memoryUsage, long memoryDiff) {
        LogUtil.logPerformance(operationName, duration);
        LogUtil.info("Memory usage for " + operationName + ": " + memoryUsage + " bytes");
        LogUtil.info("Memory difference for " + operationName + ": " + memoryDiff + " bytes");
    }

    private static void savePerformanceData(String operationName, long duration, long memoryUsage, long memoryDiff) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String dataPath = PERFORMANCE_LOG_PATH + "performance-data.csv";
            
            String csvLine = String.format("%s,%s,%d,%d,%d\n", 
                timestamp, operationName, duration, memoryUsage, memoryDiff);
            
            try (FileWriter writer = new FileWriter(dataPath, true)) {
                writer.write(csvLine);
            }
            
        } catch (IOException e) {
            LogUtil.error("Failed to save performance data", e);
        }
    }

    private static long extractPageLoadTime(List<LogEntry> logs) {
        // Extract page load time from browser performance logs
        // This is a simplified implementation
        return System.currentTimeMillis(); // Placeholder
    }

    /**
     * Clear performance data
     */
    public static void clearPerformanceData() {
        startTimes.clear();
        endTimes.clear();
        memoryUsage.clear();
        LogUtil.info("Performance data cleared");
    }

    /**
     * Get performance summary
     */
    public static Map<String, Object> getPerformanceSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        long totalDuration = 0;
        int operationCount = 0;
        
        for (String operation : startTimes.keySet()) {
            Long startTime = startTimes.get(operation);
            Long endTime = endTimes.get(operation);
            
            if (startTime != null && endTime != null) {
                totalDuration += (endTime - startTime);
                operationCount++;
            }
        }
        
        summary.put("totalOperations", operationCount);
        summary.put("totalDuration", totalDuration);
        summary.put("averageDuration", operationCount > 0 ? totalDuration / operationCount : 0);
        summary.put("currentMemoryUsage", getCurrentMemoryUsage());
        
        return summary;
    }
} 