package com.wooteco.sokdak.auth.service;

import com.wooteco.sokdak.auth.domain.AuthCode;
import com.wooteco.sokdak.auth.domain.Ticket;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthCodeRepository authCodeRepository;
    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final Encryptor encryptor;

    public AuthService(AuthCodeRepository authCodeRepository,
                       MemberRepository memberRepository,
                       TicketRepository ticketRepository, Encryptor encryptor) {
        this.authCodeRepository = authCodeRepository;
        this.memberRepository = memberRepository;
        this.ticketRepository = ticketRepository;
        this.encryptor = encryptor;
    }

    public AuthInfo login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = encryptor.encrypt(loginRequest.getPassword());
        Member member = memberRepository.findByUsernameValueAndPassword(username, password)
                .orElseThrow(LoginFailedException::new);
        return new AuthInfo(member.getId(), member.getRoleType().getName(), member.getNickname().getValue());
    }

    @Transactional
    public void useTicket(String email) {
        String serialNumber = encryptor.encrypt(email);
        Ticket ticket = ticketRepository.findBySerialNumber(serialNumber)
                .orElseThrow(SerialNumberNotFoundException::new);
        ticket.use();
    }

    public void verifyAuthCode(VerificationRequest verificationRequest) {
        String serialNumber = encryptor.encrypt(verificationRequest.getEmail());
        AuthCode authCode = authCodeRepository.findBySerialNumber(serialNumber)
                .orElseThrow(SerialNumberNotFoundException::new);
        authCode.verify(verificationRequest.getCode());
    }

    public void validateSignUpMember(String serialNumber) {
        validateQualified(serialNumber);
        validateNewMember(serialNumber);
    }

    public void validateQualified(String serialNumber) {
        boolean exist = ticketRepository.existsBySerialNumber(serialNumber);
        if (!exist) {
            throw new NotWootecoMemberException();
        }
    }

    private void validateNewMember(String serialNumber) {
        Ticket ticket = ticketRepository.findBySerialNumber(serialNumber)
                .orElseThrow(NotWootecoMemberException::new);
        if (ticket.isUsed()) {
            throw new TicketUsedException();
        }
    }
}
