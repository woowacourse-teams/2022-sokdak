package com.wooteco.sokdak.profile.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import com.wooteco.sokdak.profile.dto.EditedNicknameRequest;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.profile.exception.DuplicateNicknameException;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProfileControllerTest extends ControllerTest {

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
                .findNickname(refEq(AUTH_INFO));

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
        EditedNicknameRequest editedNicknameRequest = new EditedNicknameRequest("chrisNick2");
        doNothing()
                .when(profileService)
                .editNickname(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(editedNicknameRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("닉네임 변경 시 이미 있는 닉네임이면 400 반환")
    @Test
    void editNickname_Exception_Duplicate() {
        EditedNicknameRequest editedNicknameRequest = new EditedNicknameRequest("hunchNickname");
        doThrow(new DuplicateNicknameException())
                .when(profileService)
                .editNickname(refEq(editedNicknameRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(editedNicknameRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/fail/duplicate"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("닉네임 변경 시 잘못된 형식이면 400 반환")
    @Test
    void editNickname_Exception_InvalidFormat() {
        EditedNicknameRequest editedNicknameRequest = new EditedNicknameRequest("");
        doThrow(new InvalidNicknameException())
                .when(profileService)
                .editNickname(refEq(editedNicknameRequest), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer chris")
                .body(editedNicknameRequest)
                .when().patch("/members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("member/patch/nickname/fail/invalidFormat"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
