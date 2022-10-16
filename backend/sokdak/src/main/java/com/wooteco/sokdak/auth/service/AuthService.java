package com.wooteco.sokdak.auth.service;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.ticket.domain.AuthCode;
import com.wooteco.sokdak.ticket.domain.Ticket;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.exception.LoginFailedException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.NotWootecoMemberException;
import com.wooteco.sokdak.member.exception.SerialNumberNotFoundException;
import com.wooteco.sokdak.member.exception.TicketUsedException;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.repository.TicketRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Long APPLICANT_BOARD_ID = 5L;
    private final MemberRepository memberRepository;

    public AuthService(AuthCodeRepository authCodeRepository,
                       MemberRepository memberRepository,
                       TicketRepository ticketRepository, Clock clock) {
        this.memberRepository = memberRepository;
    }

    public AuthInfo login(LoginRequest loginRequest) {
        String username = Encryptor.encrypt(loginRequest.getUsername());
        String password = Encryptor.encrypt(loginRequest.getPassword());
        Member member = memberRepository.findByUsernameValueAndPasswordValue(username, password)
                .orElseThrow(LoginFailedException::new);
        return new AuthInfo(member.getId(), member.getRoleType().getName(), member.getNickname());
    }

    public void checkAuthority(AuthInfo authInfo, Long boardId) {
        if (!RoleType.APPLICANT.getName().equals(authInfo.getRole())) {
            return;
        }
        if (boardId == APPLICANT_BOARD_ID) {
            return;
        }
        throw new AuthorizationException();
    }
}
