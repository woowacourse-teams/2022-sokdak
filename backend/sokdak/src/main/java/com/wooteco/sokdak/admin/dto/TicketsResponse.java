package com.wooteco.sokdak.admin.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TicketsResponse {

    private List<TicketElement> tickets;

    protected TicketsResponse() {
    }

    public TicketsResponse(List<TicketElement> tickets) {
        this.tickets = tickets;
    }

    public static TicketsResponse of(List<TicketElement> tickets) {
        return new TicketsResponse(tickets);
    }
}
