package com.wooteco.sokdak.profile.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.profile.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.profile.exception.DuplicateNicknameException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfileService {

    public MemberRepository memberRepository;

    public ProfileService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void editNickname(NicknameUpdateRequest nicknameUpdateRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        Nickname validNickname = new Nickname(nicknameUpdateRequest.getNickname());
        validateUniqueNickname(validNickname);
        member.updateNickname(validNickname);
    }

    private void validateUniqueNickname(Nickname validNickname) {
        if (memberRepository.existsMemberByNickname(validNickname)) {
            throw new DuplicateNicknameException();
        }
    }

    public NicknameResponse findNickname(AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        return NicknameResponse.of(member);
    }
}
