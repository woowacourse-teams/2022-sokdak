package com.wooteco.sokdak.member.util;

import net.bytebuddy.utility.RandomString;

public class AuthCodeGenerator {

    private AuthCodeGenerator() {
    }

    public static String generate(int length) {
        return RandomString.make(length);
//                .toString()
//                .substring(0, length);
    }
}
