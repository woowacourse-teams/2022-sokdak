package com.wooteco.sokdak.comment.controller;

import static com.wooteco.sokdak.util.fixture.BoardFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.NewReplyRequest;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
import com.wooteco.sokdak.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CommentControllerTest extends ControllerTest {

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
                .apply(document("comment/create/success"))
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("대댓글 작성 요청이 오면 댓글애 새로운 대댓글을 작성한다.")
    @Test
    void addReply() {
        NewReplyRequest newReplyRequest = new NewReplyRequest(FREE_BOARD_ID, "대댓글", true);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(newReplyRequest)
                .when().post("/comments/1/reply")
                .then().log().all()
                .apply(document("reply/create/success"))
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("댓글 작성 요청에 댓글 내용이 없는 경우 400을 반환한다")
    @Test
    void addComment_Exception_NoMessage() {
        NewCommentRequest newCommentRequest = new NewCommentRequest(null, true);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(newCommentRequest)
                .when().post("/posts/1/comments")
                .then().log().all()
                .apply(document("comment/create/fail/noMessage"))
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("특정 글의 댓글 조회 요청이 오면 모든 댓글들을 반환한다.")
    @Test
    void findComments() {
        ReplyResponse replyResponse1 = new ReplyResponse(3L, "이스트1", "대댓글1", LocalDateTime.now(), false, false, false,
                2, false);
        ReplyResponse replyResponse2 = new ReplyResponse(4L, "이스트2", "대댓글2", LocalDateTime.now(), false, false, false,
                3, true);
        CommentResponse commentResponse1 = new CommentResponse(1L, "조시1", "댓글1", LocalDateTime.now(), false, false,
                false, 3, false, List.of(replyResponse1, replyResponse2));
        CommentResponse commentResponse2 = new CommentResponse(2L, "조시2", "댓글2", LocalDateTime.now(), false, true,
                false, 2, true, Collections.emptyList());
        doReturn(new CommentsResponse(List.of(commentResponse1, commentResponse2), 4))
                .when(commentService)
                .findComments(any(), any());

        restDocs
                .when().get("/posts/1/comments")
                .then().log().all()
                .apply(document("comment/find/all/success"))
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("특정 댓글 삭제 요청이 오면 해당 댓글을 삭제한다.")
    @Test
    void deleteComment() {
        doNothing()
                .when(commentService)
                .deleteComment(any(), any());

        restDocs
                .when().delete("/comments/1")
                .then().log().all()
                .apply(document("comment/delete/success"))
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
