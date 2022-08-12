package com.wooteco.sokdak.admin.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getAdminToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.admin.dto.PostReportElement;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AdminAcceptanceTest extends AcceptanceTest {

    private static final int reportCount = 5;

    @DisplayName("관리자 페이지에서 여러 DB 세팅을 할 수 있다...")
    @Test
    void adminGogo() {
        String adminToken = getAdminToken();
//      블락
        httpPostWithAuthorization(new Object(), "admin/posts/1/postReports/" + reportCount, adminToken);
//      블락목록 검색
        ExtractableResponse<Response> response = httpGetWithAuthorization("admin/postReports",
                adminToken);
        List<PostReportElement> postReports = getPostReports(response);
        assertThat(postReports).hasSize(reportCount);
    }

    private List<PostReportElement> getPostReports(ExtractableResponse<Response> response) {
        return response.body()
                .jsonPath()
                .getList("postReports", PostReportElement.class);
    }
}
