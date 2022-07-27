package com.wooteco.sokdak.member.domain;

import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import com.wooteco.sokdak.member.exception.InvalidUsernameException;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction.MAX;

@Getter
@Embeddable
public class Nickname {

    private static final Pattern PATTERN = Pattern.compile("^[0-9a-zA-Z가-힣]+(?:\\s+[0-9a-zA-Z가-힣]+)*$");
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 16;

    @Column(name = "nickname")
    private String value;

    protected Nickname() {
    }

    public Nickname(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH
        || !PATTERN.matcher(value).matches()) {
            throw new InvalidNicknameException();
        }
    }
}
