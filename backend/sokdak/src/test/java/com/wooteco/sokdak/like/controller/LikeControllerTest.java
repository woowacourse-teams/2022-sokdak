package com.wooteco.sokdak.like.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.MemberFixture.SESSION_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.support.AuthInfoMapper;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;


class LikeControllerTest extends ControllerTest {

    @MockBean
    private AuthInfoMapper authInfoMapper;

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(AUTH_INFO)
                .when(authInfoMapper)
                .getAuthInfo(any());
    }

    @DisplayName("좋아요 .")
    @Test
    void flipLike() {
        doReturn(new LikeFlipResponse(1, true))
                .when(likeService)
                .flipLike(any(), any());

        restDocs
                .sessionId(SESSION_ID)
                .sessionAttr("member", AUTH_INFO)
                .when().post("/posts/1/like")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
