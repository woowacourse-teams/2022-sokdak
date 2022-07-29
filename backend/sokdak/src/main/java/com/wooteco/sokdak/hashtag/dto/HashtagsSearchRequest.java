package com.wooteco.sokdak.hashtag.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class HashtagsSearchRequest {

    private String include;
    @Positive
    private int limit;

    protected HashtagsSearchRequest() {}

    public HashtagsSearchRequest(String include, int limit) {
        this.include = include;
        this.limit = limit;
    }
}
