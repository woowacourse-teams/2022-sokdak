package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostRequest {

    @NotBlank(message = "제목은 1자 이상 50자 이하여야 합니다.")
    private String title;
    @NotBlank(message = "본문은 1자 이상 5000자 이하여야 합니다.")
    private String content;
    private List<String> hashtags;

    public NewPostRequest() {
    }

    public NewPostRequest(String title, String content, List<String> hashtags) {
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
