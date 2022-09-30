package com.wooteco.sokdak.member.service;

import com.wooteco.sokdak.ticket.domain.AuthCode;
import com.wooteco.sokdak.auth.service.AuthCodeGenerator;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.ticket.service.RegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmailService {

    private final AuthCodeGenerator authCodeGenerator;
    private final EmailSender emailSender;
    private final AuthService authService;
    private final RegisterService registerService;
    private final AuthCodeRepository authCodeRepository;


    public EmailService(AuthCodeGenerator authCodeGenerator,
                        AuthService authService, EmailSender emailSender,
                        RegisterService registerService,
                        AuthCodeRepository authCodeRepository) {
        this.authCodeGenerator = authCodeGenerator;
        this.authService = authService;
        this.emailSender = emailSender;
        this.registerService = registerService;
        this.authCodeRepository = authCodeRepository;
    }

    @Transactional
    public void sendCodeToValidUser(EmailRequest emailRequest) {
        String serialNumber = Encryptor.encrypt(emailRequest.getEmail());
        registerService.validateSignUpMember(serialNumber);

        String authCode = createAndSaveAuthCode(serialNumber);
        sendEmail(emailRequest, authCode);
    }

    private String createAndSaveAuthCode(String serialNumber) {
        authCodeRepository.deleteAllBySerialNumber(serialNumber);
        String authCode = authCodeGenerator.generate();
        authCodeRepository.save(new AuthCode(authCode, serialNumber));
        return authCode;
    }

    private void sendEmail(EmailRequest emailRequest, String authCode) {
        emailSender.send(emailRequest.getEmail(), authCode);
    }
}
