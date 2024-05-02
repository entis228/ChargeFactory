package com.entis.app;

import com.entis.app.entity.auth.request.RefreshTokenRequest;
import com.entis.app.entity.auth.request.SignInRequest;
import com.entis.app.entity.auth.response.AccessTokenResponse;
import com.entis.app.entity.station.StationState;
import com.entis.app.entity.station.request.EditStationRequest;
import com.entis.app.entity.user.UserStatus;
import com.entis.app.entity.user.request.*;
import com.entis.app.entity.user.response.UserResponse;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource("/application-h2db.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"h2db"})
class StartApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    private String accessToken;
    private String refreshToken;

    @PostConstruct
    void registerAuthorization() {
        rest.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        String email = "user@gmail.com";
        String password = "123456";
        SaveUserRequest registrationRequest = new SaveUserRequest(email, password, "testUser");
        ResponseEntity<JsonNode> response = rest.postForEntity(URI.create(Routes.USERS), registrationRequest, JsonNode.class);
        SignInRequest inRequest = new SignInRequest(email, password);
        ResponseEntity<AccessTokenResponse> tokenResponse = rest.postForEntity(URI.create(Routes.TOKEN), inRequest, AccessTokenResponse.class);
        accessToken = tokenResponse.getBody().accessToken();
        refreshToken = tokenResponse.getBody().refreshToken();
    }

    @Test
    void contextLoads() {
        assertNotNull(rest);
    }

    @Test
    void checkGetRequestsUnauthorized() {
        ResponseEntity<String> result = rest.getForEntity(Routes.USERS, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        result = rest.getForEntity(Routes.USERS + "/current", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        result = rest.getForEntity(Routes.USERS + "/current/charges", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        result = rest.getForEntity(Routes.USERS + "/4", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        result = rest.getForEntity(Routes.USERS + "/vas@gmail.com", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        result = rest.getForEntity(Routes.USERS + "/4/charges", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        result = rest.getForEntity(Routes.STATIONS, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        result = rest.getForEntity(Routes.STATIONS + "/4", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    void checkRefreshTokens() {
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        ResponseEntity<AccessTokenResponse> tokenResponse = rest.postForEntity(URI.create(Routes.TOKEN + "/refresh"), request, AccessTokenResponse.class);
        assertEquals(HttpStatus.OK, tokenResponse.getStatusCode());
        assertNotNull(tokenResponse.getBody().accessToken());
        assertNotNull(tokenResponse.getBody().refreshToken());
        accessToken = tokenResponse.getBody().accessToken();
        refreshToken = tokenResponse.getBody().refreshToken();
    }

    @Test
    void checkCurrentUser() {
        ResponseEntity<UserResponse> response = rest.exchange(
                RequestEntity.get(URI.create(Routes.USERS + "/current"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).build(),
                UserResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testUser", response.getBody().name());
        assertEquals("user@gmail.com", response.getBody().email());
    }

    @Test
    void checkChangeCurrentUserInfo() {
        ChangeUserInfoRequest request = new ChangeUserInfoRequest("user12@gmail.com", "Alex", "Entis", "0682282281");
        ResponseEntity<UserResponse> response = rest.exchange(
                RequestEntity.patch(URI.create(Routes.USERS + "/current"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(request),
                UserResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request.email(), response.getBody().email());
        assertEquals(request.name(), response.getBody().name());
        assertEquals(request.phone(), response.getBody().phone());
        assertEquals(request.surname(), response.getBody().surname());
    }

    @Test
    void checkTopUp() {
        var request = new TopUpAccountRequest("27.01");
        ResponseEntity<UserResponse> response = rest.exchange(
                RequestEntity.post(URI.create(Routes.USERS + "/current/add"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(request),
                UserResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(27.01d, response.getBody().balance());
        request = new TopUpAccountRequest("-27.01");
        ResponseEntity<String> badResponse = rest.exchange(
                RequestEntity.post(URI.create(Routes.USERS + "/current/add"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(request),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, badResponse.getStatusCode());
        ResponseEntity<UserResponse> responseBalance = rest.exchange(
                RequestEntity.get(URI.create(Routes.USERS + "/current"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).build(),
                UserResponse.class);
        assertEquals(HttpStatus.OK, responseBalance.getStatusCode());
        assertEquals(27.01d, responseBalance.getBody().balance());
    }

    @Test
    void checkChangeCurrentPassword() {
        var request = new ChangeUserPasswordRequest("123456", "654321");
        ResponseEntity<UserResponse> response = rest.exchange(
                RequestEntity.patch(URI.create(Routes.USERS + "/current/password"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(request),
                UserResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SignInRequest inRequest = new SignInRequest("user@gmail.com", "654321");
        ResponseEntity<AccessTokenResponse> tokenResponse = rest.postForEntity(URI.create(Routes.TOKEN), inRequest, AccessTokenResponse.class);
        assertEquals(HttpStatus.OK, tokenResponse.getStatusCode());
        this.accessToken = tokenResponse.getBody().accessToken();
        this.refreshToken = tokenResponse.getBody().refreshToken();
        request = new ChangeUserPasswordRequest("654321", "123456");
        rest.exchange(
                RequestEntity.patch(URI.create(Routes.USERS + "/current/password"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(request),
                UserResponse.class);
    }

    @Test
    void checkRegularUserAccessToUpperFunctions() {
        ResponseEntity<String> response = rest.exchange(
                RequestEntity.get(URI.create(Routes.USERS) + "?page=1&size=20")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        SaveUserRequest saveUserRequest = new SaveUserRequest("lexaasket@gmal.com", "123456", "Lexa");
        response = rest.exchange(
                RequestEntity.post(URI.create(Routes.USERS + "/admins"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(saveUserRequest),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.post(URI.create(Routes.USERS + "/owners"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(saveUserRequest),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ChangeUserStatusRequest changeUserStatusRequest = new ChangeUserStatusRequest(UserStatus.SUSPENDED);
        response = rest.exchange(
                RequestEntity.patch(URI.create(Routes.USERS + "/id=4/status"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(changeUserStatusRequest),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        SetUserPasswordRequest setUserPasswordRequest = new SetUserPasswordRequest("adminClown1147");
        response = rest.exchange(
                RequestEntity.patch(URI.create(Routes.USERS + "/id=4/password"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(setUserPasswordRequest),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.get(URI.create(Routes.USERS) + "/id=4")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.delete(URI.create(Routes.USERS) + "/id=4")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.get(URI.create(Routes.USERS) + "/id=4/charges")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.get(URI.create(Routes.USERS) + "/email=da@gmail.com")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.get(URI.create(Routes.STATIONS + "?page=1&size=20"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.get(URI.create(Routes.STATIONS + "/id=4"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.post(URI.create(Routes.STATIONS))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body("Baza"),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        EditStationRequest editStationRequest = new EditStationRequest("test", StationState.CHARGING);
        response = rest.exchange(
                RequestEntity.patch(URI.create(Routes.STATIONS + "/id=4"))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(editStationRequest),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        response = rest.exchange(
                RequestEntity.delete(URI.create(Routes.STATIONS) + "/id=4")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .build(),
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}