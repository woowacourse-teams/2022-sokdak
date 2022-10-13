package com.wooteco.sokdak.auth.domain.encryptor;

@FunctionalInterface
public interface Encoder {
    String encode(String text);
}
