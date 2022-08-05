package com.wooteco.sokdak.admin.service;

import com.wooteco.sokdak.admin.dto.EmailsAddRequest;
import com.wooteco.sokdak.advice.UnauthorizedException;
import com.wooteco.sokdak.auth.domain.Ticket;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final TicketRepository ticketRepository;
    private final Encryptor encryptor;

    public AdminService(TicketRepository ticketRepository, Encryptor encryptor) {
        this.ticketRepository = ticketRepository;
        this.encryptor = encryptor;
    }

    @Transactional
    public void registerEmail(AuthInfo authInfo, EmailsAddRequest emailsAddRequest) {
        if (authInfo.getRole().equals(RoleType.USER.getName())) {
            throw new UnauthorizedException("이메일 등록은 관리자만 가능합니다.");
        }

        List<String> emails = emailsAddRequest.getEmails();

        for (String email : emails) {
            String serialNumber = encryptor.encrypt(email);
            Optional<Ticket> foundSerialNumber = ticketRepository.findBySerialNumber(serialNumber);
            if (foundSerialNumber.isEmpty()) {
                Ticket ticket = Ticket.builder()
                        .serialNumber(serialNumber)
                        .used(false)
                        .build();
                ticketRepository.save(ticket);
            }
        }
    }
}
