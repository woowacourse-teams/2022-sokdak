package com.wooteco.sokdak.auth.domain.encryptor;

@FunctionalInterface
public interface EncryptorI {
    String encode(String text);
}
