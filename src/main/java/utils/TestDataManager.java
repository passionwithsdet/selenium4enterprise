package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestDataManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> loadMap(String resourcePath) {
        try (InputStream is = TestDataManager.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) return Collections.emptyMap();
            return objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            LogUtil.error("Failed to load test data map from: " + resourcePath, e);
            return Collections.emptyMap();
        }
    }

    public static List<Map<String, Object>> loadList(String resourcePath) {
        try (InputStream is = TestDataManager.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) return Collections.emptyList();
            return objectMapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            LogUtil.error("Failed to load test data list from: " + resourcePath, e);
            return Collections.emptyList();
        }
    }
} 