package com.wooteco.sokdak.auth;

import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class SignupHelper {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password1!";
    public static final String NICKNAME = "nickname";

    private static MemberRepository memberRepository;
    private static Encryptor encryptor;

    public SignupHelper(MemberRepository memberRepository, Encryptor encryptor) {
        SignupHelper.memberRepository = memberRepository;
        SignupHelper.encryptor = encryptor;
    }

    public static void signUp() {
        Member member = Member.builder()
                .username(USERNAME)
                .password(encryptor.encrypt(PASSWORD))
                .nickname(NICKNAME)
                .build();
        memberRepository.save(member);
    }
}
