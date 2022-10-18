package com.wooteco.sokdak.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import com.wooteco.sokdak.util.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 id에 해당하는 닉네임 값 반환")
    @Test
    void findNicknameValueById() {
        String actual = memberRepository.findNicknameValueById(member1.getId())
                .get();

        assertThat(actual).isEqualTo("chrisNickname");
    }

    @DisplayName("중복된 닉네임으로 회원가입할 수 없다.")
    @Test
    void prevent_Duplicated_Nickname() {
        Member duplicatedNicknameMember = Member.builder()
                .username(Username.of(encryptor,"josh"))
                .password(Password.of(encryptor, "Abcd123!@"))
                .nickname(new Nickname("chrisNickname"))
                .build();

        assertThatThrownBy(() -> memberRepository.save(duplicatedNicknameMember))
                .isInstanceOf(DataIntegrityViolationException.class);

    }
}
