package com.wooteco.sokdak.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.ticket.domain.AuthCode;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.exception.DuplicateNicknameException;
import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @Autowired
    private MemberRepository memberRepository;

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

        assertThat(memberRepository.findByUsernameValueAndPasswordValue(Encryptor.encrypt("josh"),
                Encryptor.encrypt("Abcd123!@"))).isPresent();
    }

    @DisplayName("닉네임 수정 기능")
    @Test
    void editNickname() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("chrisNick2");

        memberService.editNickname(nicknameUpdateRequest, AUTH_INFO);

        Member member = memberRepository.findById(AUTH_INFO.getId()).orElseThrow();
        assertThat(member.getNickname()).isEqualTo("chrisNick2");
    }

    @DisplayName("존재하는 닉네임으로 수정할 시 예외 발생")
    @Test
    void editNickname_Exception_Duplicate() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("hunchNickname");

        assertThatThrownBy(() -> memberService.editNickname(nicknameUpdateRequest, AUTH_INFO))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @DisplayName("숫자와 영문, 한글음절을 포함한 1자 이상 16자이하가 아닌 잘못된 형식의 닉네임으로 수정할 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "ㄱㄴㄷ", "abdf123ㅏㅇ", "11112222333344445"})
    void editNickname_Exception_InvalidFormat(String invalidNickname) {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest(invalidNickname);

        assertThatThrownBy(() -> memberService.editNickname(nicknameUpdateRequest, AUTH_INFO))
                .isInstanceOf(InvalidNicknameException.class);
    }
}
