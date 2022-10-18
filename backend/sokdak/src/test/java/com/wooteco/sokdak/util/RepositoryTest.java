package com.wooteco.sokdak.util;

import static com.wooteco.sokdak.util.fixture.MemberFixture.ENCRYPTOR;

import com.wooteco.sokdak.auth.domain.encryptor.EncryptorFactory;
import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
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

    protected EncryptorI encryptor = ENCRYPTOR;

    @BeforeEach
    void inputData() {
        member1 = Member.builder()
                .username(Username.of(encryptor, "chris"))
                .password(Password.of(encryptor, "Abcd123!@"))
                .nickname(new Nickname("chrisNickname"))
                .build();
        memberRepository.save(member1);
    }

    @AfterEach
    void cleanUp() {
        memberRepository.delete(member1);
    }
}
