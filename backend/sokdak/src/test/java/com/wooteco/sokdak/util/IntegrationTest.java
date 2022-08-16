package com.wooteco.sokdak.util;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class IntegrationTest {

    protected static final AuthInfo AUTH_INFO = new AuthInfo(1L, RoleType.USER.getName(), "chrisNickname");
    protected static final AuthInfo AUTH_INFO2 = new AuthInfo(3L, RoleType.USER.getName(), "joshNickname");

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MemberRepository memberRepository;

    protected Member member;

    @BeforeEach
    void cleanAndSetData() {
        databaseCleaner.clear();
        databaseCleaner.insertInitialData();
        member = memberRepository.findById(1L)
                .orElseThrow(MemberNotFoundException::new);
    }
}
