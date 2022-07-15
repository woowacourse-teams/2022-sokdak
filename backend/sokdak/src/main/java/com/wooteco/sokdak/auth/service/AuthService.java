package com.wooteco.sokdak.auth.service;

import static com.wooteco.sokdak.member.util.Encryptor.encrypt;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.member.domain.member.Member;
import com.wooteco.sokdak.member.domain.member.Username;
import com.wooteco.sokdak.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public AuthInfo login(LoginRequest loginRequest) {
        Username username = new Username(loginRequest.getUsername());
        String password = encrypt(loginRequest.getPassword());
        Member member = memberRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(LoginFailedException::new);
        return new AuthInfo(member.getId());
    }
}
