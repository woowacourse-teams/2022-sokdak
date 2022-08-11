package com.wooteco.sokdak.admin.dto;

import com.wooteco.sokdak.auth.domain.Ticket;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TicketElement {

    private final Long id;
    private final String serialNumber;
    private final boolean used;

    @Builder
    public TicketElement(Long id, String serialNumber, boolean used) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.used = used;
    }

    public static TicketElement of(Ticket ticket) {
        return TicketElement.builder()
                .id(ticket.getId())
                .serialNumber(ticket.getSerialNumber())
                .used(ticket.isUsed())
                .build();
    }
}
