package com.wooteco.sokdak.member.domain;

import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.exception.InvalidPasswordFormatException;
import com.wooteco.sokdak.member.exception.InvalidUsernameException;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Password {

    private static final Pattern PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$");

    @Column(name = "password")
    private String value;

    protected Password() {
    }

    public Password(String value) {
        validate(value);
        this.value = Encryptor.encrypt(value);
    }

    private void validate(String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidPasswordFormatException();
        }
    }
}
