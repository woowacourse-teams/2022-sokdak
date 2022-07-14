package com.wooteco.sokdak.member.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.member.exception.InvalidPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @DisplayName("패스워드는 영어, 숫자, 특수문자를 포함한 8자 이상 20 이하가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"abcd12#", "abcd123abcd123abcd123", "abcd1234", "abcd!!!!", "1234!!!!", "", " "})
    void create_Exception_Format(String invalidPassword) {
        assertThatThrownBy(() -> new Password(invalidPassword))
                .isInstanceOf(InvalidPasswordException.class);
    }
}
