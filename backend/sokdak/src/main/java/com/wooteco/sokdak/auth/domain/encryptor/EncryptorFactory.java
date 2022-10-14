package com.wooteco.sokdak.auth.domain.encryptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptorFactory {

    private static final SHA256 SHA_256 = new SHA256();
    private static final Encryptor ENCRYPTOR = new Encryptor(SHA_256);

    public static Encryptor encryptor() {
        return ENCRYPTOR;
    }

    @Bean
    public Encryptor getEncryptor() {
        return ENCRYPTOR;
    }

    @Bean
    public Encoder getSha256() {
        return SHA_256;
    }
}
