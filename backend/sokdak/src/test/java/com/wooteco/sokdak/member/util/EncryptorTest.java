package com.wooteco.sokdak.member.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.auth.service.Encryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EncryptorTest {

    @DisplayName("SHA-256 해싱을 이용하면 똑같은 문자열엔 똑같은 결과가 나온다.")
    @Test
    void encrypt_same_input() {
        String text = "bjuuuu98@gmail.com";

        String encrypt1 = Encryptor.encrypt(text);
        String encrypt2 = Encryptor.encrypt(text);

        assertThat(encrypt1).isEqualTo(encrypt2);
    }

    @DisplayName("SHA-256 해싱을 이용하면 다른 문자열엔 다른 결과가 나온다.")
    @Test
    void encrypt_different_input() {
        String text = "안녕하세요";
        String other = "반갑습니다";

        String encrypt1 = Encryptor.encrypt(text);
        String encrypt2 = Encryptor.encrypt(other);

        assertThat(encrypt1).isNotEqualTo(encrypt2);
    }
}
