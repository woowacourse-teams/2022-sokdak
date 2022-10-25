package com.wooteco.sokdak.util;

import static com.wooteco.sokdak.util.fixture.MemberFixture.CHRIS_ID;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.AuthCodeGenerator;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.service.EmailSender;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    protected static final AuthInfo AUTH_INFO = new AuthInfo(CHRIS_ID, RoleType.USER.getName(), "chrisNickname");
    protected static final AuthInfo AUTH_INFO2 = new AuthInfo(4L, RoleType.USER.getName(), "joshNickname");
    protected static final AuthInfo APPLICANT_AUTH_INFO = new AuthInfo(10L, RoleType.APPLICANT.getName(), "applicantNickname");

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    protected AuthCodeGenerator authCodeGenerator;

    @MockBean
    protected EmailSender emailSender;

    @SpyBean
    protected Clock clock;

    protected Member member;

    @BeforeEach
    void cleanAndSetData() {
        databaseCleaner.clear();
        databaseCleaner.insertInitialData();
        member = memberRepository.findById(CHRIS_ID)
                .orElseThrow(MemberNotFoundException::new);
    }
}
