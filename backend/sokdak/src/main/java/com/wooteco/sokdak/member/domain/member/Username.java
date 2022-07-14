package com.wooteco.sokdak.member.domain.member;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Embeddable
public class Username {

    @Column(name = "username")
    @Length(min = 4, max = 16)
    private String value;

    protected Username() {
    }

    public Username(String value) {
        this.value = value;
    }
}
