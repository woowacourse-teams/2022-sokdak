package com.wooteco.sokdak.auth.domain.encryptor;

import com.wooteco.sokdak.member.exception.ExternalLibraryException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 implements Encoder {

    @Override
    public String encode(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new ExternalLibraryException("SHA-256");
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
