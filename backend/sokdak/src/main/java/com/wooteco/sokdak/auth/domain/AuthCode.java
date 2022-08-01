package com.wooteco.sokdak.auth.domain;

import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AuthCode {

    private static final long VALID_MINUIT = 5L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String serialNumber;

    @CreatedDate
    private LocalDateTime createdAt;

    protected AuthCode() {
    }

    @Builder
    public AuthCode(String code, String serialNumber, LocalDateTime createdAt) {
        this.code = code;
        this.serialNumber = serialNumber;
        this.createdAt = createdAt;
    }

    public AuthCode(String code, String serialNumber) {
        this.code = code;
        this.serialNumber = serialNumber;
    }

    public void verify(String code) {
        if (!this.code.equals(code)) {
            throw new InvalidAuthCodeException();
        }
    }

    public void verifyTime(LocalDateTime time) {
        LocalDateTime expireTime = this.createdAt.plusMinutes(VALID_MINUIT);
        if (time.isAfter(expireTime)) {
            throw new InvalidAuthCodeException();
        }
    }
}
