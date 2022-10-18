package com.wooteco.sokdak.member.domain;

import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.member.exception.InvalidUsernameException;
import java.util.Objects;
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
        this.value = value;
    }

    public static Username of(EncryptorI encryptorI, String value) {
        validate(value);
        return new Username(encryptorI.encode(value));
    }

    private static void validate(String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidUsernameException();
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
        Username username = (Username) o;
        return getValue().equals(username.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
