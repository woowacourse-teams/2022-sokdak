package com.wooteco.sokdak.member.domain;

import com.wooteco.sokdak.member.exception.InvalidUsernameException;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Username {

    private static final Pattern PATTERN = Pattern.compile("^[0-9a-zA-Z]{4,16}$");

    @Column(name = "username")
    private String value;

    protected Username() {
    }

    public Username(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidUsernameException();
        }
    }
}
