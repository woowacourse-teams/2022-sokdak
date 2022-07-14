package com.wooteco.sokdak.member.acceptance;

import static com.wooteco.sokdak.util.HttpMethodFixture.httpPost;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 인수테스트")
class MemberAcceptanceTest extends AcceptanceTest {
//    @DisplayName("이메일로 우테코 회원임을 인즈")
//    @Test
//    void send() {
////        이메일 인증
//        EmailRequest emailRequest = new EmailRequest("sokdak@gmail.com");
//        ExtractableResponse<Response> response = httpPost(emailRequest, "/signup/email");
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
    //        인증번호 확인
    // TODO:
//        아이디 중복 확인
//        회원가입 완료
}
