package com.wooteco.sokdak.member.dto;

import com.wooteco.sokdak.member.domain.Member;
import lombok.Getter;

@Getter
public class NicknameResponse {

    private String nickname;

    public NicknameResponse() {
    }

    public NicknameResponse(String nickname) {
        this.nickname = nickname;
    }

    public static NicknameResponse of(Member member) {
        return new NicknameResponse(member.getNickname());
    }
}
