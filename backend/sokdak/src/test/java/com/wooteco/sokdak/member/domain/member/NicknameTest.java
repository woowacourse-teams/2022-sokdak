package com.wooteco.sokdak.member.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NicknameTest {

    @DisplayName("유저의 닉네임은 숫자와 영문, 한글음절을 포함한 1자 이상 16자가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "ㄱㄴㄷ", "abdf123ㅏㅇ", "11112222333344445"})
    void create_Exception_Format(String invalidUsername) {
        assertThatThrownBy(() -> new Nickname(invalidUsername))
                .isInstanceOf(InvalidNicknameException.class);
    }
}
