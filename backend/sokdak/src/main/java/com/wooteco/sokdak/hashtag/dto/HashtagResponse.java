package com.wooteco.sokdak.hashtag.dto;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import lombok.Getter;

@Getter
public class HashtagResponse {

    private Long id;
    private String name;

    public HashtagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public HashtagResponse(Hashtag hashtag) {
        this(hashtag.getId(), hashtag.getName());
    }
}
