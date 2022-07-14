package com.wooteco.sokdak.member.repository;

import com.wooteco.sokdak.member.domain.auth.Ticket;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);
}
