package com.wooteco.sokdak.like.controller;

import static com.wooteco.sokdak.util.fixture.BoardFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.like.dto.LikeFlipRequest;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.util.ControllerTest;
import com.wooteco.sokdak.util.fixture.BoardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class PostLikeControllerTest extends ControllerTest {

    @DisplayName("좋아요를 작성할 수 있다.")
    @Test
    void flipLikePost() {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(FREE_BOARD_ID);
        LikeFlipResponse likeFlipResponse = new LikeFlipResponse(1, true);
        doReturn(likeFlipResponse)
                .when(likeService)
                .flipPostLike(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(likeFlipRequest)
                .when().put("/posts/1/like")
                .then().log().all()
                .apply(document("like/post/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("댓글에 좋아요를 작성할 수 있다.")
    @Test
    void flipLikeComment() {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(FREE_BOARD_ID);
        LikeFlipResponse likeFlipResponse = new LikeFlipResponse(1, true);
        doReturn(likeFlipResponse)
                .when(likeService)
                .flipCommentLike(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(likeFlipRequest)
                .when().put("/comments/1/like")
                .then().log().all()
                .apply(document("like/comment/success"))
                .statusCode(HttpStatus.OK.value());
    }
}
