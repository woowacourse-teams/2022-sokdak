package com.wooteco.sokdak.member.util;

import com.wooteco.sokdak.member.exception.ExternalLibraryException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    public static String encrypt(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new ExternalLibraryException("SHA-256");
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
