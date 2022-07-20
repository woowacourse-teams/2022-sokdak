package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Hashtag;
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
