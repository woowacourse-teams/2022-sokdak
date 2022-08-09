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
                .password("Abcd123!@")
                .nickname("chrisNickname")
                .build();
        memberRepository.save(member);
    }

    @AfterEach
    void cleanUp() {
        memberRepository.delete(member);
    }

}
