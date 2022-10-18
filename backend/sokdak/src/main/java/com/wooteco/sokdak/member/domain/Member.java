package com.wooteco.sokdak.member.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;

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

    @Embedded
    private Password password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.USER;

    public Member() {
    }

    @Builder
    public Member(Long id, Username username, Password password, Nickname nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public static Member applicant(Username username, Password password, Nickname nickname) {
        Member member = new Member(null, username, password, nickname);
        member.roleType = RoleType.APPLICANT;
        return member;
    }

    public void updateNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public boolean hasId(Long id) {
        return this.id.equals(id);
    }

    public String getUsername() {
        return username.getValue();
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public RoleType getRoleType() {
        return roleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Member member = (Member) o;
        return getId().equals(member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
