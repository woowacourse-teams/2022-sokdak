package com.wooteco.sokdak.ticket.service;

import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.NotWootecoMemberException;
import com.wooteco.sokdak.member.exception.SerialNumberNotFoundException;
import com.wooteco.sokdak.member.exception.TicketUsedException;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.member.repository.TicketRepository;
import com.wooteco.sokdak.ticket.domain.AuthCode;
import com.wooteco.sokdak.ticket.domain.Ticket;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {

    private final TicketRepository ticketRepository;
    private final AuthCodeRepository authCodeRepository;
    private final Clock clock;
    private final EncryptorI encryptor;

    public RegisterService(TicketRepository ticketRepository,
                           AuthCodeRepository authCodeRepository, Clock clock,
                           EncryptorI encryptor) {
        this.ticketRepository = ticketRepository;
        this.authCodeRepository = authCodeRepository;
        this.clock = clock;
        this.encryptor = encryptor;
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
        LocalDateTime now = LocalDateTime.now(clock);
        authCode.verifyTime(now);
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
