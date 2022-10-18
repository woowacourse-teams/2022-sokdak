package com.wooteco.sokdak.auth.domain.encryptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptorFactory {

    private static final EncryptorI SHA_256 = new Encryptor();

    public static EncryptorI encryptor() {
        return SHA_256;
    }

    @Bean
    public EncryptorI getSha256() {
        return SHA_256;
    }
}
