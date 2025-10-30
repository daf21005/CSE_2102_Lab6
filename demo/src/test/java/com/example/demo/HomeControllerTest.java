package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for HomeController.
 *
 * These tests start the application on a random port and use TestRestTemplate
 * to perform HTTP requests against the running server. We verify both
 * query-parameter based handlers ("say-hi-back") and JSON-based handlers
 * ("password-quality", "email-address-valid").
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

    // Inject the random port the embedded server is started on.
    @LocalServerPort
    int port;

    // TestRestTemplate is a convenient client tailored for Spring Boot tests.
    @Autowired
    TestRestTemplate rest;

    // Helper to construct the base URL for requests
    private String baseUrl() {
        return "http://localhost:" + port;
    }

    /**
     * Verify that providing a `data` query parameter returns an "Echo" message
     * containing the provided value. This tests the @RequestParam flow.
     */
    @Test
    void sayHiBack_withData_returnsEcho() {
        // Build the URI with proper encoding instead of manually inserting %20
        var uri = UriComponentsBuilder.fromUriString(baseUrl() + "/say-hi-back")
                .queryParam("data", "Hello there")
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> resp = rest.postForEntity(uri, null, String.class);
        // Expect HTTP 200 OK
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        // Body should include the echoed text
        assertTrue(resp.getBody().contains("Echo: Hello there"));
    }

    /**
     * Verify that omitting the `data` parameter uses the controller's defaultValue.
     */
    @Test
    void sayHiBack_withoutData_usesDefault() {
        ResponseEntity<String> resp = rest.postForEntity(baseUrl() + "/say-hi-back", null, String.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        // Controller defaultValue contains the phrase "Enjoy your day"
        assertTrue(resp.getBody().contains("Enjoy your day"));
    }

    /**
     * Submit a JSON body to the password-quality endpoint and assert the
     * controller recognizes a strong password.
     */
    @Test
    void passwordQuality_strongPassword_returnsStrong() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"password\":\"MyP@ssw0rd!\"}"; // strong password example
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> resp = rest.postForEntity(baseUrl() + "/password-quality", entity, String.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Strong password."));
    }

    /**
     * Send a JSON payload to the email-address-valid endpoint and assert the
     * controller reports the email as valid.
     */
    @Test
    void emailAddressValid_withJson_returnsValid() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"email\":\"foo@bar.com\"}";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> resp = rest.postForEntity(baseUrl() + "/email-address-valid", entity, String.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Valid email address."));
    }
}
