package com.wooteco.sokdak.member.domain.member;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Embeddable
public class Password {

    @Column(name = "password")
    @Length(min = 8, max = 20)
    private String value;

    protected Password() {
    }

    public Password(String value) {
        this.value = value;
    }
}
