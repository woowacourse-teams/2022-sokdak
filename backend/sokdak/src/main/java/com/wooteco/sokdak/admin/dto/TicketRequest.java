package com.wooteco.sokdak.admin.dto;

import com.wooteco.sokdak.ticket.domain.Ticket;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TicketRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String serialNumber;
    @NotBlank(message = "사용 여부를 입력해주세요.")
    private boolean used;

    protected TicketRequest(){
    }

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
