package com.wooteco.sokdak.util.fixture;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpMethodFixture {

    private static final String REQUEST_URI_PREFIX = "/api";

    public static ExtractableResponse<Response> httpGet(String path) {
        return RestAssured
                .given().log().all()
                .when().get(REQUEST_URI_PREFIX + path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetWithCookie(String path, String cookieValue) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.COOKIE, cookieValue)
                .when().get(REQUEST_URI_PREFIX + path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPost(Object postRequest, String path) {
        return RestAssured
                .given().log().all()
                .body(postRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(REQUEST_URI_PREFIX + path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().get(REQUEST_URI_PREFIX + path)
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
                .when().post(REQUEST_URI_PREFIX + path)
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
                .when().put(REQUEST_URI_PREFIX + path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPutWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().put(REQUEST_URI_PREFIX + path)
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
                .when().patch(REQUEST_URI_PREFIX + path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpDeleteWithAuthorization(String path, String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, token)
                .when().delete(REQUEST_URI_PREFIX + path)
                .then().log().all()
                .extract();
    }

    public static String getExceptionMessage(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("message");
    }
}
