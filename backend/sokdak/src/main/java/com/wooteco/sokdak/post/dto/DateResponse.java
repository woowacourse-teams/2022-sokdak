package com.wooteco.sokdak.post.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class DateResponse {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final String date;
    private final String time;

    private DateResponse(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public static DateResponse from(LocalDateTime localDateTime) {
        String date = localDateTime.format(DATE_FORMATTER);
        String time = localDateTime.format(TIME_FORMATTER);
        return new DateResponse(date, time);
    }
}
