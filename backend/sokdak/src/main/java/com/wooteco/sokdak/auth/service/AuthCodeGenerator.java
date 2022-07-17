package com.wooteco.sokdak.auth.service;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeGenerator {

    public String generate(int length) {
        return RandomString.make(length);
    }
}
