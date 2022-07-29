package com.wooteco.sokdak.board.repository;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostBoardRepository extends JpaRepository<PostBoard, Long> {

    Optional<PostBoard> findPostBoardByPostAndBoard(Post post, Board board);

    Slice<PostBoard> findPostBoardsByBoardId(Long boardId, Pageable pageable);
}
