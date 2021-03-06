package com.wooteco.sokdak.comment.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.MemberFixture.SESSION_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CommentControllerTest extends ControllerTest {

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());

        doReturn(AUTH_INFO)
                .when(authenticationPrincipalArgumentResolver)
                .resolveArgument(any(), any(), any(), any());
    }

    @DisplayName("댓글 작성 요청이 오면 새로운 댓글을 작성한다.")
    @Test
    void addComment() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
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

    @DisplayName("특정 글의 댓글 조회 요청이 오면 모든 댓글들을 반환한다.")
    @Test
    void findComments() {
        CommentResponse commentResponse1 = new CommentResponse(1L, "조시1", "댓글1", LocalDateTime.now());
        CommentResponse commentResponse2 = new CommentResponse(2L, "조시2", "댓글2", LocalDateTime.now());
        doReturn(new CommentsResponse(List.of(commentResponse1, commentResponse2)))
                .when(commentService)
                .findComments(any());

        restDocs
                .when().get("/posts/1/comments")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
