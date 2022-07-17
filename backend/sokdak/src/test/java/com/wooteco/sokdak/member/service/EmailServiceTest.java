package com.wooteco.sokdak.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.wooteco.sokdak.auth.domain.Ticket;
import com.wooteco.sokdak.member.dto.EmailRequest;
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

//    @Autowired
//    AuthCodeRepository authCodeRepository;

//    @Autowired
//    TicketRepository ticketRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    EmailSender emailSender;

    @MockBean
    TicketRepository ticketRepository;

//    @BeforeEach
//    void setUp() {
//        Ticket ticket = Ticket.builder()
//                .serialNumber("bjuuuu98@gmail.com")
//                .used(false)
//                .build();
//        ticketRepository.save(ticket);
//    }

    // TODO: 실제 이메일 보내는 기능은 모킹하고 검증하는 로직 테스트 하도록 변경
    @DisplayName("특정 이메일로 인증코드 발송")
    @Test
    void sendEmail() {
        Ticket ticket = Ticket.builder().serialNumber("serialNumber").used(false).build();

        TicketRepository ticketRepository = mock(TicketRepository.class);
        EmailRequest emailRequest = new EmailRequest("bjuuuu98@gmail.com");
//        String serialNumber = Encryptor.encrypt(emailRequest.getEmail());
//
//        when(ticketRepository.existsBySerialNumber(anyString()))
//                .thenReturn(true);
//
//        when(ticketRepository.findBySerialNumber(anyString()))
//                .thenReturn(Optional.ofNullable(ticket));
//
//        when(emailService.getAuthCode(emailRequest))
//                .thenReturn("a1b2c3");

        emailService.sendCodeToValidUser(emailRequest);
        verify(emailSender).send("bjuuuu98@gmail.com", "a1b2c3");
    }
}
