package com.wooteco.sokdak.member.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded
    private Username username;

    @Embedded
    private Nickname nickname;

    private String password;

    public Member() {
    }

    @Builder
    public Member(Long id, String username, String password, String nickname) {
        this.id = id;
        this.username = new Username(username);
        this.password = password;
        this.nickname = new Nickname(nickname);
    }
}
