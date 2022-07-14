package com.wooteco.sokdak.member.domain.member;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Nickname {

    @Column(name = "nickname")
    private String value;

    protected Nickname() {
    }

    public Nickname(String value) {
        this.value = value;
    }
}
