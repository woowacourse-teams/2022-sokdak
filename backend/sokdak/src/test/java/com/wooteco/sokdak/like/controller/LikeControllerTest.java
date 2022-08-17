package com.wooteco.sokdak.like.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


class LikeControllerTest extends ControllerTest {

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(AUTH_INFO)
                .when(authenticationPrincipalArgumentResolver)
                .resolveArgument(any(), any(), any(), any());
    }

    @DisplayName("좋아요를 작성할 수 있다.")
    @Test
    void flipLike() {
        doReturn(new LikeFlipResponse(1, true))
                .when(likeService)
                .flipLikePost(any(), any());

        restDocs
                .when().put("/posts/1/like")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
