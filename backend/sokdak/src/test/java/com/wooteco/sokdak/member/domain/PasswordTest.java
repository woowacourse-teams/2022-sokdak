package com.wooteco.sokdak.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.member.exception.InvalidPasswordFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @DisplayName("유저의 비밀번호는 영어 대소문자, 숫자, 특수문자(@$!%*#?&) 세 카테고리를 모두 포함하는 8자이상 20자 이하의 문자열이 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "abcdAbcd", "abCd1234", "12341234", "abcd12!", "abcd1234^"})
    void create_Exception_Format(String invalidPassword) {
        assertThatThrownBy(() -> new Password(invalidPassword))
                .isInstanceOf(InvalidPasswordFormatException.class);
    }
}
