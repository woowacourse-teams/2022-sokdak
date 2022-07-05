package com.wooteco.sokdak.post.controller.dto;

import lombok.Getter;

@Getter
public class DateResponse {

    private String date;
    private String time;

    public DateResponse(String date, String time) {
        this.date = date;
        this.time = time;
    }
}
