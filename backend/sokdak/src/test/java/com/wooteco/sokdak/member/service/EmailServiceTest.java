package com.wooteco.sokdak.member.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.ENCRYPTOR;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.repository.TicketRepository;
import com.wooteco.sokdak.ticket.domain.Ticket;
import com.wooteco.sokdak.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmailServiceTest extends ServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TicketRepository ticketRepository;

    @DisplayName("인증 코드 발송")
    @Test
    void sendCodeToValidUser() {
        given(authCodeGenerator.generate())
                .willReturn("a1b1c1");
        Ticket ticket = Ticket.builder()
                .serialNumber(ENCRYPTOR.encrypt("test@gmail.com"))
                .used(false)
                .build();
        ticketRepository.save(ticket);

        emailService.sendCodeToValidUser(new EmailRequest("test@gmail.com"));

        verify(emailSender, times(1)).send("test@gmail.com", "a1b1c1");
    }
}
