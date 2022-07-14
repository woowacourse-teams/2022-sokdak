package com.wooteco.sokdak.member.service;

import com.wooteco.sokdak.member.dto.UsernameUniqueResponse;
import com.wooteco.sokdak.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UsernameUniqueResponse checkUnique(String username) {
        boolean unique = !memberRepository.existsByUsername(username);
        return new UsernameUniqueResponse(unique);
    }
}
