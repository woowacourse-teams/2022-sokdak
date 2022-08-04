package com.wooteco.sokdak.auth.domain;

import com.wooteco.sokdak.support.token.InvalidRefreshTokenException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "token")
    private String token;

    protected RefreshToken() {
    }

    public RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public void validateSameToken(String token) {
        if (!this.token.equals(token)) {
            throw new InvalidRefreshTokenException();
        }
    }
}
