package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {
    private static final Logger logger = LogManager.getLogger(LogUtil.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        setupLogging();
    }

    private static void setupLogging() {
        try {
            String logPath = ConfigReader.get("log.file.path", "target/logs/");
            Path logDir = Paths.get(logPath);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
        } catch (IOException e) {
            System.err.println("Failed to create log directory: " + e.getMessage());
        }
    }

    public static void info(String message) {
        logger.info(formatMessage("INFO", message));
    }

    public static void warn(String message) {
        logger.warn(formatMessage("WARN", message));
    }

    public static void error(String message) {
        logger.error(formatMessage("ERROR", message));
    }

    public static void error(String message, Throwable throwable) {
        logger.error(formatMessage("ERROR", message), throwable);
    }

    public static void debug(String message) {
        logger.debug(formatMessage("DEBUG", message));
    }

    public static void trace(String message) {
        logger.trace(formatMessage("TRACE", message));
    }

    public static void fatal(String message) {
        logger.fatal(formatMessage("FATAL", message));
    }

    private static String formatMessage(String level, String message) {
        return String.format("[%s] [%s] [%s] %s", 
            LocalDateTime.now().format(formatter),
            level,
            Thread.currentThread().getName(),
            message);
    }

    public static void logTestStart(String testName) {
        info("=== TEST STARTED: " + testName + " ===");
    }

    public static void logTestEnd(String testName, String status) {
        info("=== TEST " + status + ": " + testName + " ===");
    }

    public static void logStep(String step) {
        info("STEP: " + step);
    }

    public static void logElementAction(String element, String action) {
        debug("ELEMENT ACTION: " + action + " on " + element);
    }

    public static void logPageNavigation(String url) {
        info("NAVIGATING TO: " + url);
    }

    public static void logScreenshot(String screenshotPath) {
        info("SCREENSHOT CAPTURED: " + screenshotPath);
    }

    public static void logPerformance(String operation, long duration) {
        info("PERFORMANCE: " + operation + " took " + duration + "ms");
    }
} 