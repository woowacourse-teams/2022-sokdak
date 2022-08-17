package com.wooteco.sokdak.util.fixture;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class HttpMethodFixture {

    public static ExtractableResponse<Response> httpGet(String path) {
        return RestAssured
                .given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPost(Object postRequest, String path) {
        return RestAssured
                .given().log().all()
                .body(postRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPostWithAuthorization(
            Object requestBody, String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPutWithAuthorization(
            Object requestBody, String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPutWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPatchWithAuthorization(
            Object requestBody, String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpDeleteWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().delete(path)
                .then().log().all()
                .extract();
    }

    public static String getExceptionMessage(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("message");
    }

    public static String getChrisToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }

    public static String getJoshToken() {
        LoginRequest loginRequest = new LoginRequest("josh", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }

    public static String getAdminToken() {
        LoginRequest loginRequest = new LoginRequest("east", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }
}
