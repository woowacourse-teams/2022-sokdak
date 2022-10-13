package com.wooteco.sokdak.auth.domain.encryptor;

public class Encryptor {

    private final Encoder encoder;

    public Encryptor(Encoder encoder) {
        this.encoder = encoder;
    }

    public static String encrypt(String text) {
        return EncryptorFactory.encryptor().encode(text);
    }

    public String encode(String text) {
        return encoder.encode(text);
    }
}
