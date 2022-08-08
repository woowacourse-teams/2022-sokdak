package com.wooteco.sokdak.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.auth.domain.AuthCode;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.util.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Encryptor encryptor;

    @DisplayName("이미 존재하는 username이면 uniqueResponse의 unique를 false, 이외는 true를 반환한다.")
    @ParameterizedTest
    @CsvSource({"josh, false", "hahahoho, true"})
    void checkUniqueUsername(String username, boolean expected) {
        UniqueResponse uniqueResponse = memberService.checkUniqueUsername(username);

        assertThat(uniqueResponse.isUnique()).isEqualTo(expected);
    }

    @DisplayName("회원가입 조건을 모두 만족하면 회원가입에 성공한다.")
    @Test
    void signUp() {
        AuthCode authCode = AuthCode.builder()
                .code("ABCDEF")
                .serialNumber("21f46568bf6002c23843d198af30bb2bc8123695bd3d12ce86e0fc35bc5d3279")
                .build();
        authCodeRepository.save(authCode);

        SignupRequest signupRequest = new SignupRequest("whgusrms96@gmail.com", "testJosh",
                "testJoshNickname", "ABCDEF", "Abcd123!@", "Abcd123!@");
        memberService.signUp(signupRequest);

        assertThat(memberRepository.findByUsernameAndPassword(encryptor.encrypt("josh"),
                encryptor.encrypt("Abcd123!@"))).isPresent();
    }
}
