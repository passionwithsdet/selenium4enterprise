package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiUtil {
    private static final HttpClient httpClient = HttpClients.createDefault();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private String baseUrl;
    private Map<String, String> defaultHeaders;
    private Map<String, String> defaultParams;
    
    public ApiUtil(String baseUrl) {
        this.baseUrl = baseUrl;
        this.defaultHeaders = new HashMap<>();
        this.defaultParams = new HashMap<>();
        setupDefaultHeaders();
    }
    
    private void setupDefaultHeaders() {
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("User-Agent", "Enterprise-Selenium-Framework/1.0");
    }
    
    // GET Request
    public ApiResponse get(String endpoint) {
        return get(endpoint, new HashMap<>(), new HashMap<>());
    }
    
    public ApiResponse get(String endpoint, Map<String, String> headers) {
        return get(endpoint, headers, new HashMap<>());
    }
    
    public ApiResponse get(String endpoint, Map<String, String> headers, Map<String, String> params) {
        try {
            String url = buildUrl(endpoint, params);
            HttpGet request = new HttpGet(url);
            
            // Add headers
            addHeaders(request, headers);
            
            LogUtil.info("Making GET request to: " + url);
            HttpResponse response = httpClient.execute(request);
            
            return createApiResponse(response);
            
        } catch (Exception e) {
            LogUtil.error("GET request failed for endpoint: " + endpoint, e);
            return new ApiResponse(500, "Request failed: " + e.getMessage(), null);
        }
    }
    
    // POST Request
    public ApiResponse post(String endpoint, String body) {
        return post(endpoint, body, new HashMap<>());
    }
    
    public ApiResponse post(String endpoint, String body, Map<String, String> headers) {
        try {
            String url = buildUrl(endpoint, new HashMap<>());
            HttpPost request = new HttpPost(url);
            
            // Add headers
            addHeaders(request, headers);
            
            // Add body
            if (body != null && !body.isEmpty()) {
                request.setEntity(new StringEntity(body, "UTF-8"));
            }
            
            LogUtil.info("Making POST request to: " + url);
            LogUtil.info("Request body: " + body);
            
            HttpResponse response = httpClient.execute(request);
            return createApiResponse(response);
            
        } catch (Exception e) {
            LogUtil.error("POST request failed for endpoint: " + endpoint, e);
            return new ApiResponse(500, "Request failed: " + e.getMessage(), null);
        }
    }
    
    // PUT Request
    public ApiResponse put(String endpoint, String body) {
        return put(endpoint, body, new HashMap<>());
    }
    
    public ApiResponse put(String endpoint, String body, Map<String, String> headers) {
        try {
            String url = buildUrl(endpoint, new HashMap<>());
            HttpPut request = new HttpPut(url);
            
            // Add headers
            addHeaders(request, headers);
            
            // Add body
            if (body != null && !body.isEmpty()) {
                request.setEntity(new StringEntity(body, "UTF-8"));
            }
            
            LogUtil.info("Making PUT request to: " + url);
            LogUtil.info("Request body: " + body);
            
            HttpResponse response = httpClient.execute(request);
            return createApiResponse(response);
            
        } catch (Exception e) {
            LogUtil.error("PUT request failed for endpoint: " + endpoint, e);
            return new ApiResponse(500, "Request failed: " + e.getMessage(), null);
        }
    }
    
    // DELETE Request
    public ApiResponse delete(String endpoint) {
        return delete(endpoint, new HashMap<>());
    }
    
    public ApiResponse delete(String endpoint, Map<String, String> headers) {
        try {
            String url = buildUrl(endpoint, new HashMap<>());
            HttpDelete request = new HttpDelete(url);
            
            // Add headers
            addHeaders(request, headers);
            
            LogUtil.info("Making DELETE request to: " + url);
            
            HttpResponse response = httpClient.execute(request);
            return createApiResponse(response);
            
        } catch (Exception e) {
            LogUtil.error("DELETE request failed for endpoint: " + endpoint, e);
            return new ApiResponse(500, "Request failed: " + e.getMessage(), null);
        }
    }
    
    // PATCH Request
    public ApiResponse patch(String endpoint, String body) {
        return patch(endpoint, body, new HashMap<>());
    }
    
    public ApiResponse patch(String endpoint, String body, Map<String, String> headers) {
        try {
            String url = buildUrl(endpoint, new HashMap<>());
            HttpPatch request = new HttpPatch(url);
            
            // Add headers
            addHeaders(request, headers);
            
            // Add body
            if (body != null && !body.isEmpty()) {
                request.setEntity(new StringEntity(body, "UTF-8"));
            }
            
            LogUtil.info("Making PATCH request to: " + url);
            LogUtil.info("Request body: " + body);
            
            HttpResponse response = httpClient.execute(request);
            return createApiResponse(response);
            
        } catch (Exception e) {
            LogUtil.error("PATCH request failed for endpoint: " + endpoint, e);
            return new ApiResponse(500, "Request failed: " + e.getMessage(), null);
        }
    }
    
    // Utility methods
    private String buildUrl(String endpoint, Map<String, String> params) {
        StringBuilder url = new StringBuilder(baseUrl);
        if (!endpoint.startsWith("/")) {
            url.append("/");
        }
        url.append(endpoint);
        
        // Add query parameters
        if (!params.isEmpty()) {
            url.append("?");
            for (Map.Entry<String, String> param : params.entrySet()) {
                url.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
            url.deleteCharAt(url.length() - 1); // Remove last &
        }
        
        return url.toString();
    }
    
    private void addHeaders(HttpRequestBase request, Map<String, String> headers) {
        // Add default headers
        for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }
        
        // Add custom headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }
    }
    
    private ApiResponse createApiResponse(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        
        LogUtil.info("Response status: " + statusCode);
        LogUtil.info("Response body: " + responseBody);
        
        return new ApiResponse(statusCode, responseBody, response.getAllHeaders());
    }
    
    // JSON utility methods
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            LogUtil.error("Failed to serialize object to JSON", e);
            return "{}";
        }
    }
    
    public JsonNode parseJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            LogUtil.error("Failed to parse JSON", e);
            return null;
        }
    }
    
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            LogUtil.error("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
            return null;
        }
    }
    
    // Authentication methods
    public void setBearerToken(String token) {
        defaultHeaders.put("Authorization", "Bearer " + token);
    }
    
    public void setBasicAuth(String username, String password) {
        String credentials = java.util.Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes());
        defaultHeaders.put("Authorization", "Basic " + credentials);
    }
    
    public void setApiKey(String apiKey) {
        defaultHeaders.put("X-API-Key", apiKey);
    }
    
    // Response validation methods
    public boolean isSuccess(ApiResponse response) {
        return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
    }
    
    public boolean isClientError(ApiResponse response) {
        return response.getStatusCode() >= 400 && response.getStatusCode() < 500;
    }
    
    public boolean isServerError(ApiResponse response) {
        return response.getStatusCode() >= 500;
    }
    
    // Inner class for API Response
    public static class ApiResponse {
        private final int statusCode;
        private final String body;
        private final org.apache.http.Header[] headers;
        
        public ApiResponse(int statusCode, String body, org.apache.http.Header[] headers) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
        
        public String getBody() {
            return body;
        }
        
        public org.apache.http.Header[] getHeaders() {
            return headers;
        }
        
        public String getHeader(String name) {
            if (headers != null) {
                for (org.apache.http.Header header : headers) {
                    if (header.getName().equalsIgnoreCase(name)) {
                        return header.getValue();
                    }
                }
            }
            return null;
        }
        
        @Override
        public String toString() {
            return "ApiResponse{" +
                    "statusCode=" + statusCode +
                    ", body='" + body + '\'' +
                    '}';
        }
    }
} 