package com.wooteco.sokdak.member.domain;

import com.wooteco.sokdak.member.exception.InvalidAuthCodeException;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AuthCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String serialNumber;

    @CreatedDate
    private LocalDateTime createdAt;

    protected AuthCode() {
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
}
