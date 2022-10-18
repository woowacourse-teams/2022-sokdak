package com.wooteco.sokdak.member.domain;

public enum RoleType {
    APPLICANT("APPLICANT"), USER("USER"), ADMIN("ADMIN");

    private String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isNot(String roleType) {
        return !this.name.equals(roleType);
    }
}
