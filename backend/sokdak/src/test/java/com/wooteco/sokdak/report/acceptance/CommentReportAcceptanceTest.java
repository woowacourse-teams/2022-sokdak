package com.wooteco.sokdak.report.acceptance;

import static com.wooteco.sokdak.post.util.CommentFixture.*;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("댓글 신고 관련 인수테스트")
class CommentReportAcceptanceTest extends AcceptanceTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest("나쁜댓글");

    @DisplayName("댓글을 신고할 수 있다.")
    @Test
    void reportComment() {
        Long commentId = addCommentAndGetCommentId();

        ExtractableResponse<Response> response = httpPostWithAuthorization(REPORT_REQUEST,
                "/comments/" + commentId + "/report", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Long addCommentAndGetCommentId() {
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT,
                Collections.emptyList());
        Long postId = Long.parseLong(httpPostWithAuthorization(newPostRequest, "/posts", getToken())
                .header("Location").split("/posts/")[1]);

        NewCommentRequest newCommentRequest = new NewCommentRequest(VALID_COMMENT_MESSAGE, true);
        httpPostWithAuthorization(newCommentRequest, "/posts/" + postId + "/comments", getToken());
        // addComment 해주는 api에 Location을 안넣어줘서 id를 알 방법이 없음.(repository 사용 안하면서)
        // 그래서 하드코딩으로 리턴
        return 1L;
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }
}
