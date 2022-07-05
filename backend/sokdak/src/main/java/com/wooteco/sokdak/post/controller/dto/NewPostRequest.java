package com.wooteco.sokdak.post.controller.dto;

import lombok.Getter;

@Getter
public class NewPostRequest {

    private String title;
    private String content;

    public NewPostRequest() {
    }

    public NewPostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
