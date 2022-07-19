package com.wooteco.sokdak.member.repository;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 id에 해당하는 닉네임 값 반환")
    @Test
    void findNicknameValueById() {
        Member member = Member.builder()
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        memberRepository.save(member);

        String actual = memberRepository.findNicknameValueById(member.getId())
                .get();

        assertThat(actual).isEqualTo(VALID_NICKNAME);
    }
}