package utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";
    private static final String ENV_PROPERTY = "env";
    private static final String DEFAULT_ENV = "qa";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        String environment = System.getProperty(ENV_PROPERTY, DEFAULT_ENV);
        
        try {
            // Load main config file
            InputStream mainConfig = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (mainConfig != null) {
                properties.load(mainConfig);
                mainConfig.close();
            }

            // Load environment-specific config
            String envConfigFile = environment + ".properties";
            InputStream envConfig = ConfigReader.class.getClassLoader().getResourceAsStream(envConfigFile);
            if (envConfig != null) {
                properties.load(envConfig);
                envConfig.close();
            }

            // Apply encryption if enabled
            if (Boolean.parseBoolean(get("encrypt.credentials", "false"))) {
                StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                encryptor.setPassword(System.getenv("ENCRYPTION_KEY") != null ? 
                    System.getenv("ENCRYPTION_KEY") : "defaultKey");
                properties = new EncryptableProperties(properties, encryptor);
            }

        } catch (IOException e) {
            LogUtil.error("Failed to load configuration files: " + e.getMessage());
            throw new RuntimeException("Configuration loading failed", e);
        }
    }

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LogUtil.warn("Invalid integer value for key: " + key + ", using default: " + defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value.isEmpty() ? defaultValue : Boolean.parseBoolean(value);
    }

    public static String getEnvironment() {
        return get("environment", DEFAULT_ENV);
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getBrowser() {
        return get("browser", "chrome");
    }

    public static boolean isHeadless() {
        return getBoolean("headless", false);
    }

    public static boolean isParallelExecution() {
        return getBoolean("parallel.execution", true);
    }

    public static int getThreadCount() {
        return getInt("thread.count", 4);
    }

    public static boolean isGridEnabled() {
        return getBoolean("grid.enabled", false);
    }

    public static String getGridUrl() {
        return get("grid.url", "http://localhost:4444/wd/hub");
    }

    public static void reload() {
        loadProperties();
    }
}
