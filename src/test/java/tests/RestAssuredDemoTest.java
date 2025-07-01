package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class RestAssuredDemoTest {
    private String baseUrl;

    @BeforeClass
    public void setup() {
        baseUrl = "https://jsonplaceholder.typicode.com";
        RestAssured.baseURI = baseUrl;
    }

    @Test
    public void testGetUsers() {
        RestAssured.given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", Matchers.greaterThan(0));
    }

    @Test
    public void testGetUserByIdWithPathParam() {
        int userId = 1;
        RestAssured.given()
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(userId));
    }

    @Test
    public void testGetWithQueryParams() {
        RestAssured.given()
                .queryParam("_limit", 2)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(2));
    }

    @Test
    public void testPostCreateUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Jane Doe");
        user.put("username", "janedoe");
        user.put("email", "jane.doe@example.com");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", Matchers.equalTo("Jane Doe"));
    }

    @Test
    public void testPutUpdateUser() {
        int userId = 1;
        Map<String, Object> user = new HashMap<>();
        user.put("id", userId);
        user.put("name", "Updated Jane Doe");
        user.put("email", "updated.jane.doe@example.com");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .put("/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("Updated Jane Doe"));
    }

    @Test
    public void testPatchUser() {
        int userId = 1;
        Map<String, Object> patch = new HashMap<>();
        patch.put("name", "Patched Jane Doe");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(patch)
                .when()
                .patch("/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("Patched Jane Doe"));
    }

    @Test
    public void testDeleteUser() {
        int userId = 1;
        RestAssured.given()
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(200);
    }

    @Test
    public void testCustomHeadersAndCookies() {
        RestAssured.given()
                .header("X-Test-Header", "HeaderValue")
                .cookie("session_id", "12345")
                .when()
                .get("/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void testBasicAuth() {
        // JSONPlaceholder doesn't require auth, but demo for syntax
        RestAssured.given()
                .auth().preemptive().basic("user", "pass")
                .when()
                .get("/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void testResponseExtraction() {
        Response response = RestAssured.given()
                .when()
                .get("/users/1");
        Assert.assertEquals(response.statusCode(), 200);
        String name = response.jsonPath().getString("name");
        Assert.assertNotNull(name);
    }

    @Test
    public void testJsonSchemaValidation() {
        // Example schema validation (schema file should be in src/test/resources/schema/user-schema.json)
        RestAssured.given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/user-schema.json"));
    }
} 