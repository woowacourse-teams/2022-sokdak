package com.wooteco.sokdak.board.dto;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class BoardContentElement {

    private Long id;
    private String title;
    private List<BoardContentPostElement> posts;

    public BoardContentElement() {
    }

    public BoardContentElement(Long id, String title,
                               List<BoardContentPostElement> posts) {
        this.id = id;
        this.title = title;
        this.posts = posts;
    }

    public static BoardContentElement from(Board board, List<PostsElementResponse> postsElementResponses) {
        List<BoardContentPostElement> boardContentPostElements = postsElementResponses.stream()
                .map(it -> new BoardContentPostElement(it.getLikeCount(), it.getTitle(), it.getCommentCount()))
                .collect(Collectors.toList());
        return new BoardContentElement(board.getId(), board.getTitle(), boardContentPostElements);
    }
}
