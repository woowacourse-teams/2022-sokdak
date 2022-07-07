package com.wooteco.sokdak.post.domain;

import com.wooteco.sokdak.post.exception.InvalidPostException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;


@Getter
@Embeddable
public class Title {

    private static final int MAX_TITLE_LENGTH = 50;

    @Column(name = "title", nullable = false)
    private String value;

    protected Title() {
    }

    public Title(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPostException();
        }
        if (value.length() > MAX_TITLE_LENGTH) {
            throw new InvalidPostException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Title)) {
            return false;
        }
        Title title = (Title) o;
        return Objects.equals(value, title.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
