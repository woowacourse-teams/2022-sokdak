package com.wooteco.sokdak.util.fixture;

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

    public static ExtractableResponse<Response> httpPostWithAuthorization(
            Object requestBody, String path, String sessionId) {
        return RestAssured
                .given().log().all()
                .sessionId(sessionId)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPutWithAuthorization(
            Object requestBody, String path, String sessionId) {
        return RestAssured
                .given().log().all()
                .sessionId(sessionId)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpDeleteWithAuthorization(String path, String sessionId) {
        return RestAssured
                .given().log().all()
                .sessionId(sessionId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(path)
                .then().log().all()
                .extract();
    }

    public static String getExceptionMessage(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("message");
    }
}