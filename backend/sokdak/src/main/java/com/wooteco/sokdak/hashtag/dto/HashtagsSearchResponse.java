package com.wooteco.sokdak.hashtag.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class HashtagsSearchResponse {

    private final List<HashtagSearchElementResponse> hashtags;

    public HashtagsSearchResponse(List<HashtagSearchElementResponse> hashtags) {
        this.hashtags = hashtags;
    }
}
