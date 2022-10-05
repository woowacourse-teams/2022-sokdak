package com.wooteco.sokdak.ticket.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private String serialNumber;

    private boolean used;

    protected Ticket() {
    }

    @Builder
    private Ticket(String serialNumber, boolean used) {
        this.serialNumber = serialNumber;
        this.used = used;
    }

    public void updateUsed(boolean used) {
        this.used = used;
    }

    public void use() {
        this.used = true;
    }
}
