package com.wooteco.sokdak.hashtag.dto;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import lombok.Getter;

@Getter
public class HashtagSearchElementResponse {

    private Long id;
    private String name;
    private int count;

    public HashtagSearchElementResponse(Long id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public HashtagSearchElementResponse(Hashtag hashtag, int count) {
        this(hashtag.getId(), hashtag.getName(), count);
    }
}
