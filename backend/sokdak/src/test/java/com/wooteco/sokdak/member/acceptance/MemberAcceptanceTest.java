package com.wooteco.sokdak.member.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPatchWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.dto.NicknameResponse;
import com.wooteco.sokdak.member.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 인수테스트")
class MemberAcceptanceTest extends AcceptanceTest {

//     TODO:
//        아이디 중복 확인
//        회원가입 완료

    @DisplayName("닉네임을 수정할 수 있다.")
    @Test
    void editNickname() {
        String nickname = "chrisNick2";
        NicknameUpdateRequest nicknameRequest = new NicknameUpdateRequest(nickname);
        String token = getToken();

        ExtractableResponse<Response> response = httpPatchWithAuthorization(nicknameRequest,
                "/members/nickname", token);
        NicknameResponse nicknameResponse = toNicknameResponse(httpGetWithAuthorization("/members/nickname", token));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo(nickname)
        );
    }

    @DisplayName("중복된 닉네임으로 수정할 수 없다.")
    @Test
    void editNickname_Exception_Duplicate() {
        NicknameUpdateRequest wrongNicknameRequest = new NicknameUpdateRequest("eastNickname");
        String token = getToken();

        ExtractableResponse<Response> response = httpPatchWithAuthorization(wrongNicknameRequest,
                "/members/nickname", token);
        NicknameResponse nicknameResponse = toNicknameResponse(httpGetWithAuthorization("/members/nickname", token));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo("chrisNickname")
        );
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }

    private NicknameResponse toNicknameResponse(ExtractableResponse<Response> getWithAuthorization) {
        return getWithAuthorization.body()
                .jsonPath()
                .getObject(".", NicknameResponse.class);
    }
}
