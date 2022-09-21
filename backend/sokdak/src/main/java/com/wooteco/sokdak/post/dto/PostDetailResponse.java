package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.board.domain.PostBoard;
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

    private Long id;
    private Long boardId;
    private String nickname;
    private String title;
    private String content;
    private boolean blocked;
    private List<HashtagResponse> hashtags;
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean like;
    private boolean authorized;
    private boolean modified;
    private String imageName;

    public PostDetailResponse() {
    }

    @Builder
    private PostDetailResponse(Long id, Long boardId, String nickname, String title, String content, boolean blocked,
                               List<HashtagResponse> hashtagResponses, LocalDateTime createdAt, int likeCount,
                               boolean like, boolean authorized, boolean modified, String imageName) {
        this.id = id;
        this.boardId = boardId;
        this.blocked = blocked;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.hashtags = hashtagResponses;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.like = like;
        this.authorized = authorized;
        this.modified = modified;
        this.imageName = imageName;
    }


    public static PostDetailResponse of(Post post, PostBoard postBoard, boolean liked,
                                        boolean authorized, Hashtags hashtags, String imageName) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .boardId(postBoard.getBoard().getId())
                .nickname(post.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .blocked(post.isBlocked())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikeCount())
                .like(liked)
                .authorized(authorized)
                .hashtagResponses(toResponse(hashtags))
                .modified(post.isModified())
                .imageName(imageName)
                .build();
    }

    private static List<HashtagResponse> toResponse(Hashtags hashtags) {
        return hashtags.getValue().stream()
                .map(HashtagResponse::new)
                .collect(Collectors.toList());
    }
}
