package com.wooteco.sokdak.like.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


class LikeControllerTest extends ControllerTest {

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
        doReturn(AUTH_INFO)
                .when(authenticationPrincipalArgumentResolver)
                .resolveArgument(any(), any(), any(), any());
    }

    @DisplayName("게시글에 좋아요를 작성할 수 있다.")
    @Test
    void flipLikePost() {
        LikeFlipResponse likeFlipResponse = new LikeFlipResponse(1, true);
        doReturn(likeFlipResponse)
                .when(likeService)
                .flipPostLike(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().put("/posts/1/like")
                .then().log().all()
                .apply(document("like/post/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("댓글에 좋아요를 작성할 수 있다.")
    @Test
    void flipLikeComment() {
        LikeFlipResponse likeFlipResponse = new LikeFlipResponse(1, true);
        doReturn(likeFlipResponse)
                .when(likeService)
                .flipCommentLike(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .when().put("/comments/1/like")
                .then().log().all()
                .apply(document("like/comment/success"))
                .statusCode(HttpStatus.OK.value());
    }
}
