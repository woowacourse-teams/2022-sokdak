package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.dto.HashtagResponse;
import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
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
    private final boolean blocked;

    @Builder
    private PostDetailResponse(Long id, String title, String content, List<HashtagResponse> hashtagResponses,
                               LocalDateTime createdAt, int likeCount,
                               boolean like,
                               boolean authorized, boolean modified, boolean blocked) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hashtags = hashtagResponses;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.like = like;
        this.authorized = authorized;
        this.modified = modified;
        this.blocked = blocked;
    }


    public static PostDetailResponse of(Post post, boolean liked, boolean authorized, Hashtags hashtags) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikeCount())
                .like(liked)
                .authorized(authorized)
                .hashtagResponses(toResponse(hashtags))
                .modified(post.isModified())
                .blocked(post.isBlocked())
                .build();
    }

    private static List<HashtagResponse> toResponse(Hashtags hashtags) {
        return hashtags.getValue().stream()
                .map(HashtagResponse::new)
                .collect(Collectors.toList());
    }
}
