package com.wooteco.sokdak.post.dto;

import lombok.Getter;

@Getter
public class HashtagResponse {

    private Long id;
    private String name;

    public HashtagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
