package com.wooteco.sokdak.auth.domain.encryptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptorFactory {

    public static EncryptorI encryptor() {
        return new Encryptor();
    }

    @Bean
    public EncryptorI getEncryptor() {
        return new Encryptor();
    }
}
