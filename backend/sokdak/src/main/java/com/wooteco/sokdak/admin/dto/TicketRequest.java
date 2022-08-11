package com.wooteco.sokdak.admin.dto;

import com.wooteco.sokdak.auth.domain.Ticket;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TicketRequest {

    private final String serialNumber;
    private final boolean used;

    @Builder
    public TicketRequest(String serialNumber, boolean used) {
        this.serialNumber = serialNumber;
        this.used = used;
    }

    public static TicketRequest of(Ticket ticket) {
        return TicketRequest.builder()
                .serialNumber(ticket.getSerialNumber())
                .used(ticket.isUsed())
                .build();
    }

    public Ticket toTicket(){
        return Ticket.builder()
                .serialNumber(serialNumber)
                .used(used)
                .build();
    }
}
