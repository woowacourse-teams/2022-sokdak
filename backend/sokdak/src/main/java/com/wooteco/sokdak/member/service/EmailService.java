package com.wooteco.sokdak.member.service;

import com.wooteco.sokdak.member.domain.auth.AuthCode;
import com.wooteco.sokdak.member.domain.auth.Ticket;
import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.NotWootecoMemberException;
import com.wooteco.sokdak.member.exception.SerialNumberNotFoundException;
import com.wooteco.sokdak.member.exception.TicketUsedException;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.member.repository.TicketRepository;
import com.wooteco.sokdak.member.util.AuthCodeGenerator;
import com.wooteco.sokdak.member.util.Encryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmailService {

    private final EmailSender emailSender;
    private final TicketRepository ticketRepository;
    private final AuthCodeRepository authCodeRepository;

    public EmailService(TicketRepository ticketRepository, EmailSender emailSender,
                        AuthCodeRepository authCodeRepository) {
        this.ticketRepository = ticketRepository;
        this.emailSender = emailSender;
        this.authCodeRepository = authCodeRepository;
    }

    public void sendEmail(EmailRequest emailRequest) {
        String serialNumber = Encryptor.encrypt(emailRequest.getEmail());
        validate(serialNumber);

        authCodeRepository.deleteAllBySerialNumber(serialNumber);
        String authCode = AuthCodeGenerator.generate(6);
        emailSender.send(emailRequest.getEmail(), authCode);

        authCodeRepository.save(new AuthCode(authCode, serialNumber));
    }

    public void validate(String serialNumber) {
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

    public void verifyAuthCode(VerificationRequest verificationRequest) {
        String serialNumber = Encryptor.encrypt(verificationRequest.getEmail());
        AuthCode authCode = authCodeRepository.findBySerialNumber(serialNumber)
                .orElseThrow(SerialNumberNotFoundException::new);
        authCode.verify(verificationRequest.getCode());
    }
}
