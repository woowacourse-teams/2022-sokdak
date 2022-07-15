package com.wooteco.sokdak.member.service;

import com.wooteco.sokdak.member.domain.auth.Ticket;
import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        Ticket ticket = Ticket.builder()
                .serialNumber("bjuuuu98@gmail.com")
                .used(false)
                .build();
        ticketRepository.save(ticket);
    }

    // TODO: 실제 이메일 보내는 기능은 모킹하고 검증하는 로직 테스트 하도록 변경
    @DisplayName("특정 이메일로 인증코드 발송")
    @Test
    void sendEmail() {
        EmailRequest emailRequest = new EmailRequest("bjuuuu98@gmail.com");
        emailService.sendEmail(emailRequest);
    }
}
