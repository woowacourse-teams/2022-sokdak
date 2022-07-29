package com.wooteco.sokdak.hashtag.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class HashtagsSearchRequest {

    @NotBlank(message = "해시태그는 1자 이상 50자 이하여야 합니다.")
    private String include;
    @Positive
    private int limit;

    protected HashtagsSearchRequest() {}

    public HashtagsSearchRequest(String include, int limit) {
        this.include = include;
        this.limit = limit;
    }
}
