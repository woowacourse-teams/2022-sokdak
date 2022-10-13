package com.wooteco.sokdak.auth.domain.encryptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptorFactory {

    private static final SHA256 SHA_256 = new SHA256();
    private static final Encryptor ENCRYPTOR = new Encryptor(sha256());

    @Bean
    public static Encryptor encryptor() {
        return ENCRYPTOR;
    }

    @Bean
    public static Encoder sha256() {
        return SHA_256;
    }
}
