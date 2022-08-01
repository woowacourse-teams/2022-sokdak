package com.wooteco.sokdak.hashtag.dto;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HashtagSearchElementResponse {

    private Long id;
    private String name;
    private Long count;

    protected HashtagSearchElementResponse() {
    }

    public HashtagSearchElementResponse(Long id, String name, Long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public HashtagSearchElementResponse(Hashtag hashtag, Long count) {
        this(hashtag.getId(), hashtag.getName(), count);
    }
}
