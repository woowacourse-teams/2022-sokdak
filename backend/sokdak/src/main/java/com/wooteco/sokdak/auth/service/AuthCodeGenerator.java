package com.wooteco.sokdak.auth.service;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeGenerator {

    public String generate() {
        return String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100_000, 1_000_000));
    }
}
