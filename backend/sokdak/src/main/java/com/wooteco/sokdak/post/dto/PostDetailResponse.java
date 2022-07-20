package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Hashtag;
import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PostDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final List<HashtagResponse> hashtags;
    private final LocalDateTime createdAt;
    private final int likeCount;
    private final boolean like;
    private final boolean authorized;
    private final boolean modified;

    public PostDetailResponse(Long id, String title, String content, List<HashtagResponse> hashtagResponses,
                              LocalDateTime createdAt, boolean authorized, boolean modified) {
    @Builder
    private PostDetailResponse(Long id, String title, String content, LocalDateTime createdAt, int likeCount,
                               boolean like,
                               boolean authorized, boolean modified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hashtags = hashtagResponses;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.like = like;
        this.authorized = authorized;
        this.modified = modified;
    }


    public static PostDetailResponse of(Post post, boolean liked, boolean authorized) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikeCount())
                .like(liked)
                .authorized(authorized)
                .hashtags(toResponse(hashtags))
                .modified(post.isModified())
                .build();
    }

    private static List<HashtagResponse> toResponse(List<Hashtag> hashtags) {
        return hashtags.stream()
                .map(HashtagResponse::new)
                .collect(Collectors.toList());
    }
}
