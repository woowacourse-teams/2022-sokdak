package com.wooteco.sokdak.member.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPatchWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.member.dto.NicknameResponse;
import com.wooteco.sokdak.member.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 인수테스트")
class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("아이디가 유니크한지 확인 할 수 있다.")
    @ParameterizedTest
    @CsvSource({"chris, false", "another, true"})
    void validateUniqueUsername(String username, boolean expected) {
        ExtractableResponse<Response> response = httpGet("/members/signup/exists?username=" + username);
        boolean unique = response.jsonPath()
                .getObject(".", UniqueResponse.class)
                .isUnique();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(unique).isEqualTo(expected)
        );
    }

    @DisplayName("닉네임이 유니크한지 확인 할 수 있다.")
    @ParameterizedTest
    @CsvSource({"chrisNickname, false", "anotherNickname, true"})
    void validateUniqueNickname(String nickname, boolean expected) {
        ExtractableResponse<Response> response = httpGet("/members/signup/exists?nickname=" + nickname);
        boolean unique = response.jsonPath()
                .getObject(".", UniqueResponse.class)
                .isUnique();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(unique).isEqualTo(expected)
        );
    }

    @DisplayName("닉네임을 수정할 수 있다.")
    @Test
    void editNickname() {
        String nickname = "chrisNick2";
        NicknameUpdateRequest nicknameRequest = new NicknameUpdateRequest(nickname);
        String token = getChrisToken();

        ExtractableResponse<Response> response =
                httpPatchWithAuthorization(nicknameRequest, "/members/nickname", token);
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
        String token = getChrisToken();

        ExtractableResponse<Response> response = httpPatchWithAuthorization(wrongNicknameRequest,
                "/members/nickname", token);
        NicknameResponse nicknameResponse = toNicknameResponse(httpGetWithAuthorization("/members/nickname", token));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo("chrisNickname")
        );
    }

    private NicknameResponse toNicknameResponse(ExtractableResponse<Response> getWithAuthorization) {
        return getWithAuthorization.body()
                .jsonPath()
                .getObject(".", NicknameResponse.class);
    }
}
