package com.wooteco.sokdak.util;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RepositoryTest {

    @Autowired
    public MemberRepository memberRepository;

    public Member member;

    @BeforeEach
    void inputData() {
        member = Member.builder()
                .username("chris")
                .password("6297d64078fc9abcfe37d0e2c910d4798bb4c04502d7dd1207f558860c2b382e")
                .nickname("chrisNickname")
                .build();
        memberRepository.save(member);
    }

    @AfterEach
    void cleanUp() {
        memberRepository.delete(member);
    }

}
