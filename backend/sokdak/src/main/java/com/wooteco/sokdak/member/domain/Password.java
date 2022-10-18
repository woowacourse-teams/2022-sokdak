package com.wooteco.sokdak.member.domain;

import com.wooteco.sokdak.auth.domain.encryptor.Encryptor;
import com.wooteco.sokdak.member.exception.InvalidPasswordFormatException;
import java.util.Objects;
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

    public Password(Encryptor encryptor, String value) {
        validate(value);
        this.value = encryptor.encode(value);
    }

    private void validate(String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidPasswordFormatException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password = (Password) o;
        return getValue().equals(password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
