package com.wooteco.sokdak.profile.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.profile.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.profile.exception.DuplicateNicknameException;
import com.wooteco.sokdak.util.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProfileServiceTest extends IntegrationTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("닉네임 수정 기능")
    @Test
    void editNickname() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("chrisNick2");
        profileService.editNickname(nicknameUpdateRequest, AUTH_INFO);
        Member member = memberRepository.findById(AUTH_INFO.getId()).orElseThrow();

        assertThat(member.getNickname()).isEqualTo("chrisNick2");
    }

    @DisplayName("존재하는 닉네임으로 수정할 시 예외 발생")
    @Test
    void editNickname_Exception_Duplicate() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("hunchNickname");

        assertThatThrownBy(() -> profileService.editNickname(nicknameUpdateRequest,AUTH_INFO))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @DisplayName("잘못된 형식의 닉네임으로 수정할 시 예외 발생")
    @Test
    void editNickname_Exception_InvalidFormat() {
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("");

        assertThatThrownBy(() -> profileService.editNickname(nicknameUpdateRequest,AUTH_INFO))
                .isInstanceOf(InvalidNicknameException.class);
    }
}
