package com.wooteco.sokdak.board.repository;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostBoardRepository extends JpaRepository<PostBoard, Long> {

    Optional<PostBoard> findPostBoardByPostAndBoard(Post post, Board board);

    @Query(value = "SELECT p FROM Post p INNER JOIN PostBoard pb ON p=pb.post WHERE pb.board.id = :boardId")
    Slice<Post> findPostsByBoardId(Long boardId, Pageable pageable);

    List<PostBoard> findPostBoardsByPost(Post post);
}
