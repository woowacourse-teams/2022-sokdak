package com.wooteco.sokdak.auth.service;

import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Long APPLICANT_BOARD_ID = 5L;
    private final MemberRepository memberRepository;
    private final EncryptorI encryptor;

    public AuthService(MemberRepository memberRepository, EncryptorI encryptor) {
        this.memberRepository = memberRepository;
        this.encryptor = encryptor;
    }

    public AuthInfo login(LoginRequest loginRequest) {
        String username = encryptor.encrypt(loginRequest.getUsername());
        String password = encryptor.encrypt(loginRequest.getPassword());
        Member member = memberRepository.findByUsernameValueAndPasswordValue(username, password)
                .orElseThrow(LoginFailedException::new);
        return new AuthInfo(member.getId(), member.getRoleType().getName(), member.getNickname());
    }

    public void checkAuthority(AuthInfo authInfo, Long boardId) {
        if (isWootecoUser(authInfo)) {
            return;
        }
        if (isEditableBoardToApplicant(boardId)) {
            return;
        }
        throw new AuthorizationException("인증된 사용자만 이용가능 합니다.");
    }

    private boolean isWootecoUser(AuthInfo authInfo) {
        return RoleType.APPLICANT.isNot(authInfo.getRole());
    }

    private boolean isEditableBoardToApplicant(Long boardId) {
        return boardId.equals(APPLICANT_BOARD_ID);
    }
}
