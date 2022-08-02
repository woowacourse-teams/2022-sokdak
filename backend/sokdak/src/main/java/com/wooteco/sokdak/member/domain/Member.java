package com.wooteco.sokdak.member.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

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

    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.USER;

    public Member() {
    }

    @Builder
    public Member(Long id, String username, String password, String nickname) {
        this.id = id;
        this.username = new Username(username);
        this.password = password;
        this.nickname = new Nickname(nickname);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username.getValue();
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getPassword() {
        return password;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}
