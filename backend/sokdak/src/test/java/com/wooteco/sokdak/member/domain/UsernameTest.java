package com.wooteco.sokdak.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.member.exception.InvalidUsernameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UsernameTest {

    @DisplayName("유저의 아이디는 숫자와 영문만을 포함한 4자 이상 16자가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"sok", "sokdaksokdaksokkk", "가sokdak", "sokdak!", "", " "})
    void create_Exception_Format(String invalidUsername) {
        assertThatThrownBy(() -> new Username(invalidUsername))
                .isInstanceOf(InvalidUsernameException.class);
    }
}
