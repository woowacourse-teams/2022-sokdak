package com.wooteco.sokdak.member.domain.auth;

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
    @Column(name = "email_id")
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
}
