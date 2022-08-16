package com.wooteco.sokdak.util;

import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JPAConfig.class)
public class RepositoryTest {

    @Autowired
    protected MemberRepository memberRepository;

    protected Member member1;

    @BeforeEach
    void inputData() {
        member1 = Member.builder()
                .username("chris")
                .password("Abcd123!@")
                .nickname("chrisNickname")
                .build();
        memberRepository.save(member1);
    }

    @AfterEach
    void cleanUp() {
        memberRepository.delete(member1);
    }
}
