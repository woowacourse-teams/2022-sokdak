package com.wooteco.sokdak.post.controller.dto;

import lombok.Getter;

@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private DateResponse localDate;

    public PostResponse(Long id, String title, String content, DateResponse localDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.localDate = localDate;
    }
}
