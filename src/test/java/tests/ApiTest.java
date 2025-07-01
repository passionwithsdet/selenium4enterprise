package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ApiUtil;
import utils.ConfigReader;
import listeners.RetryAnalyzer;

import java.util.HashMap;
import java.util.Map;

public class ApiTest extends BaseTest {
    
    private ApiUtil apiUtil;
    private String baseUrl;
    
    @BeforeClass
    public void setupApi() {
        // Using JSONPlaceholder for demo API testing
        baseUrl = "https://jsonplaceholder.typicode.com";
        apiUtil = new ApiUtil(baseUrl);
        
        // Set authentication if needed
        // apiUtil.setBearerToken("your-token-here");
        // apiUtil.setApiKey("your-api-key-here");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetUsers() {
        logStep("Testing GET /users endpoint");
        
        ApiUtil.ApiResponse response = apiUtil.get("/users");
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying response body is not empty");
        Assert.assertNotNull(response.getBody(), "Response body should not be null");
        Assert.assertFalse(response.getBody().isEmpty(), "Response body should not be empty");
        
        logStep("Verifying response is valid JSON");
        JsonNode jsonResponse = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(jsonResponse, "Response should be valid JSON");
        Assert.assertTrue(jsonResponse.isArray(), "Response should be a JSON array");
        Assert.assertTrue(jsonResponse.size() > 0, "Response should contain users");
        
        logStep("Verifying first user structure");
        JsonNode firstUser = jsonResponse.get(0);
        Assert.assertNotNull(firstUser.get("id"), "User should have id field");
        Assert.assertNotNull(firstUser.get("name"), "User should have name field");
        Assert.assertNotNull(firstUser.get("email"), "User should have email field");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserById() {
        logStep("Testing GET /users/{id} endpoint");
        
        int userId = 1;
        ApiUtil.ApiResponse response = apiUtil.get("/users/" + userId);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying user data");
        JsonNode user = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(user, "User data should not be null");
        Assert.assertEquals(user.get("id").asInt(), userId, "User ID should match");
        Assert.assertNotNull(user.get("name").asText(), "User name should not be null");
        Assert.assertNotNull(user.get("email").asText(), "User email should not be null");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testCreateUser() {
        logStep("Testing POST /users endpoint");
        
        String userData = """
            {
                "name": "John Doe",
                "username": "johndoe",
                "email": "john.doe@example.com",
                "phone": "123-456-7890",
                "website": "johndoe.com"
            }
            """;
        
        ApiUtil.ApiResponse response = apiUtil.post("/users", userData);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201");
        
        logStep("Verifying created user data");
        JsonNode createdUser = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(createdUser, "Created user should not be null");
        Assert.assertEquals(createdUser.get("name").asText(), "John Doe", "Name should match");
        Assert.assertEquals(createdUser.get("email").asText(), "john.doe@example.com", "Email should match");
        Assert.assertNotNull(createdUser.get("id"), "User should have an ID");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateUser() {
        logStep("Testing PUT /users/{id} endpoint");
        
        int userId = 1;
        String updatedUserData = """
            {
                "id": 1,
                "name": "Updated John Doe",
                "username": "updatedjohndoe",
                "email": "updated.john.doe@example.com",
                "phone": "987-654-3210",
                "website": "updatedjohndoe.com"
            }
            """;
        
        ApiUtil.ApiResponse response = apiUtil.put("/users/" + userId, updatedUserData);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying updated user data");
        JsonNode updatedUser = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(updatedUser, "Updated user should not be null");
        Assert.assertEquals(updatedUser.get("id").asInt(), userId, "User ID should match");
        Assert.assertEquals(updatedUser.get("name").asText(), "Updated John Doe", "Name should be updated");
        Assert.assertEquals(updatedUser.get("email").asText(), "updated.john.doe@example.com", "Email should be updated");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testPatchUser() {
        logStep("Testing PATCH /users/{id} endpoint");
        
        int userId = 1;
        String patchData = """
            {
                "name": "Patched John Doe",
                "email": "patched.john.doe@example.com"
            }
            """;
        
        ApiUtil.ApiResponse response = apiUtil.patch("/users/" + userId, patchData);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying patched user data");
        JsonNode patchedUser = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(patchedUser, "Patched user should not be null");
        Assert.assertEquals(patchedUser.get("id").asInt(), userId, "User ID should match");
        Assert.assertEquals(patchedUser.get("name").asText(), "Patched John Doe", "Name should be patched");
        Assert.assertEquals(patchedUser.get("email").asText(), "patched.john.doe@example.com", "Email should be patched");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteUser() {
        logStep("Testing DELETE /users/{id} endpoint");
        
        int userId = 1;
        ApiUtil.ApiResponse response = apiUtil.delete("/users/" + userId);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying deletion response");
        // Note: JSONPlaceholder doesn't actually delete, it just returns success
        Assert.assertNotNull(response.getBody(), "Response body should not be null");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserWithQueryParams() {
        logStep("Testing GET /users with query parameters");
        
        Map<String, String> params = new HashMap<>();
        params.put("_limit", "5");
        params.put("_start", "0");
        
        ApiUtil.ApiResponse response = apiUtil.get("/users", new HashMap<>(), params);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying limited results");
        JsonNode users = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(users, "Users should not be null");
        Assert.assertTrue(users.size() <= 5, "Should return maximum 5 users");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserPosts() {
        logStep("Testing GET /users/{id}/posts endpoint");
        
        int userId = 1;
        ApiUtil.ApiResponse response = apiUtil.get("/users/" + userId + "/posts");
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying posts data");
        JsonNode posts = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(posts, "Posts should not be null");
        Assert.assertTrue(posts.isArray(), "Response should be an array");
        Assert.assertTrue(posts.size() > 0, "Should have posts");
        
        logStep("Verifying post structure");
        JsonNode firstPost = posts.get(0);
        Assert.assertNotNull(firstPost.get("id"), "Post should have id");
        Assert.assertNotNull(firstPost.get("title"), "Post should have title");
        Assert.assertNotNull(firstPost.get("body"), "Post should have body");
        Assert.assertEquals(firstPost.get("userId").asInt(), userId, "Post should belong to correct user");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserAlbums() {
        logStep("Testing GET /users/{id}/albums endpoint");
        
        int userId = 1;
        ApiUtil.ApiResponse response = apiUtil.get("/users/" + userId + "/albums");
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying albums data");
        JsonNode albums = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(albums, "Albums should not be null");
        Assert.assertTrue(albums.isArray(), "Response should be an array");
        Assert.assertTrue(albums.size() > 0, "Should have albums");
        
        logStep("Verifying album structure");
        JsonNode firstAlbum = albums.get(0);
        Assert.assertNotNull(firstAlbum.get("id"), "Album should have id");
        Assert.assertNotNull(firstAlbum.get("title"), "Album should have title");
        Assert.assertEquals(firstAlbum.get("userId").asInt(), userId, "Album should belong to correct user");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserTodos() {
        logStep("Testing GET /users/{id}/todos endpoint");
        
        int userId = 1;
        ApiUtil.ApiResponse response = apiUtil.get("/users/" + userId + "/todos");
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying todos data");
        JsonNode todos = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(todos, "Todos should not be null");
        Assert.assertTrue(todos.isArray(), "Response should be an array");
        Assert.assertTrue(todos.size() > 0, "Should have todos");
        
        logStep("Verifying todo structure");
        JsonNode firstTodo = todos.get(0);
        Assert.assertNotNull(firstTodo.get("id"), "Todo should have id");
        Assert.assertNotNull(firstTodo.get("title"), "Todo should have title");
        Assert.assertNotNull(firstTodo.get("completed"), "Todo should have completed status");
        Assert.assertEquals(firstTodo.get("userId").asInt(), userId, "Todo should belong to correct user");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testCreatePost() {
        logStep("Testing POST /posts endpoint");
        
        String postData = """
            {
                "title": "Test Post Title",
                "body": "This is a test post body for API testing",
                "userId": 1
            }
            """;
        
        ApiUtil.ApiResponse response = apiUtil.post("/posts", postData);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201");
        
        logStep("Verifying created post data");
        JsonNode createdPost = apiUtil.parseJson(response.getBody());
        Assert.assertNotNull(createdPost, "Created post should not be null");
        Assert.assertEquals(createdPost.get("title").asText(), "Test Post Title", "Title should match");
        Assert.assertEquals(createdPost.get("body").asText(), "This is a test post body for API testing", "Body should match");
        Assert.assertEquals(createdPost.get("userId").asInt(), 1, "User ID should match");
        Assert.assertNotNull(createdPost.get("id"), "Post should have an ID");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testErrorHandling() {
        logStep("Testing error handling with invalid endpoint");
        
        ApiUtil.ApiResponse response = apiUtil.get("/invalid-endpoint");
        
        logStep("Verifying error response");
        Assert.assertTrue(apiUtil.isClientError(response) || apiUtil.isServerError(response), 
                         "Should return client or server error");
        
        logStep("Verifying error response body");
        Assert.assertNotNull(response.getBody(), "Error response should have body");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testResponseHeaders() {
        logStep("Testing response headers");
        
        ApiUtil.ApiResponse response = apiUtil.get("/users");
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying content-type header");
        String contentType = response.getHeader("Content-Type");
        Assert.assertNotNull(contentType, "Content-Type header should be present");
        Assert.assertTrue(contentType.contains("application/json"), "Content-Type should be application/json");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testCustomHeaders() {
        logStep("Testing custom headers");
        
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("X-Custom-Header", "CustomValue");
        customHeaders.put("X-Test-Header", "TestValue");
        
        ApiUtil.ApiResponse response = apiUtil.get("/users", customHeaders);
        
        logStep("Verifying response status code");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        logStep("Verifying custom headers were sent");
        // Note: JSONPlaceholder doesn't echo back custom headers, but the request should succeed
        Assert.assertNotNull(response.getBody(), "Response should be successful");
    }
} 