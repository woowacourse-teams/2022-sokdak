package com.wooteco.sokdak.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.wooteco.sokdak.auth.domain.AuthCode;
import com.wooteco.sokdak.auth.domain.Ticket;
import com.wooteco.sokdak.auth.service.AuthCodeGenerator;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.repository.AuthCodeRepository;
import com.wooteco.sokdak.member.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @MockBean
    private AuthCodeGenerator authCodeGenerator;

    @MockBean
    private Encryptor encryptor;

    @MockBean
    private EmailSender emailSender;

    @DisplayName("인증 코드 저장")
    @Test
    void createAndSaveAuthCode() {
        given(authCodeGenerator.generate(6))
                .willReturn("a1b1c1");

        emailService.createAndSaveAuthCode("serialNumber");
        AuthCode authCode = authCodeRepository.findBySerialNumber("serialNumber").get();

        assertThat(authCode.getCode()).isEqualTo("a1b1c1");
    }

    @DisplayName("인증 코드 발송")
    @Test
    void sendCodeToValidUser() {
        given(encryptor.encrypt("test@gmail.com"))
                .willReturn("serialNumber");
        given(authCodeGenerator.generate(6))
                .willReturn("a1b1c1");
        Ticket ticket = Ticket.builder()
                .serialNumber("serialNumber")
                .used(false)
                .build();
        ticketRepository.save(ticket);

        emailService.sendCodeToValidUser(new EmailRequest("test@gmail.com"));

        verify(emailSender, times(1)).send("test@gmail.com", "a1b1c1");
    }
}
