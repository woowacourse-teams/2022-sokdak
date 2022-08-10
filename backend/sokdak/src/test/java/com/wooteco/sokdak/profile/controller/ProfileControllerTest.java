package com.wooteco.sokdak.profile.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.PostFixture.POSTS_ELEMENT_RESPONSE_1;
import static com.wooteco.sokdak.util.fixture.PostFixture.POSTS_ELEMENT_RESPONSE_2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import com.wooteco.sokdak.profile.dto.MyPostsResponse;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.profile.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.profile.exception.DuplicateNicknameException;
import com.wooteco.sokdak.util.ControllerTest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProfileControllerTest extends ControllerTest {

    private static final int WRONG_PAGE = 99;

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
        doReturn(AUTH_INFO)
                .when(authenticationPrincipalArgumentResolver)
                .resolveArgument(any(), any(), any(), any());
    }

    @DisplayName("닉네임 조회 시 200 반환")
    @Test
    void findNickname() {
        doReturn(new NicknameResponse("chrisNickname"))
                .when(profileService)
                .findNickname(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .when().get("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/find/nickname/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("닉네임 변경 시 204 반환")
    @Test
    void editNickname() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("chrisNick2");
        doNothing()
                .when(profileService)
                .editNickname(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(nicknameUpdateRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("닉네임 변경 시 이미 있는 닉네임이면 400 반환")
    @Test
    void editNickname_Exception_Duplicate() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("hunchNickname");
        doThrow(new DuplicateNicknameException())
                .when(profileService)
                .editNickname(refEq(nicknameUpdateRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(nicknameUpdateRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/fail/duplicate"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("닉네임 변경 시 잘못된 형식이면 400 반환")
    @Test
    void editNickname_Exception_InvalidFormat() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("");
        doThrow(new InvalidNicknameException())
                .when(profileService)
                .editNickname(refEq(nicknameUpdateRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(nicknameUpdateRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/fail/invalidFormat"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("내가 쓴 글 조회 시 200 반환")
    @Test
    void searchMyPosts() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        MyPostsResponse myPostsResponse = new MyPostsResponse(
                List.of(POSTS_ELEMENT_RESPONSE_2, POSTS_ELEMENT_RESPONSE_1),
                5);
        doReturn(myPostsResponse)
                .when(profileService)
                .findMyPosts(refEq(pageRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer any")
                .when().get("/posts/me?size=2&page=0")
                .then().log().all()
                .assertThat()
                .apply(document("member/find/posts/success/postIn"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("내가 쓴 글의 없는 페이지 조회 시 200 반환")
    @Test
    void searchMyPosts_Exception_NoPage() {
        PageRequest pageRequest = PageRequest.of(WRONG_PAGE, 2);
        doReturn(new MyPostsResponse(Collections.emptyList(), 5))
                .when(profileService)
                .findMyPosts(refEq(pageRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer any")
                .when().get("/posts/me?size=2&page=" + WRONG_PAGE)
                .then().log().all()
                .assertThat()
                .apply(document("member/find/posts/success/noPost"))
                .statusCode(HttpStatus.OK.value());
    }
}