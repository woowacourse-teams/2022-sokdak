package com.wooteco.sokdak.comment.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.MemberFixture.SESSION_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.support.AuthInfoMapper;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CommentControllerTest extends ControllerTest {

    @MockBean
    private AuthInfoMapper authInfoMapper;

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(AUTH_INFO)
                .when(authInfoMapper)
                .getAuthInfo(any());
    }

    @DisplayName("댓글 작성 요청이 오면 새로운 댓글을 작성한다.")
    @Test
    void addComment() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .sessionAttr("member", AUTH_INFO)
                .body(newCommentRequest)
                .when().post("/posts/1/comments")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("댓글 작성 요청에 댓글 내용이 없는 경우 400을 반환한다")
    @Test
    void addComment_Exception_NoMessage() {
        NewCommentRequest newCommentRequest = new NewCommentRequest(null, true);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionId(SESSION_ID)
                .sessionAttr("member", AUTH_INFO)
                .body(newCommentRequest)
                .when().post("/posts/1/comments")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
