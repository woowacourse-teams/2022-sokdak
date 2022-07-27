package com.wooteco.sokdak.board.repository;

import com.wooteco.sokdak.board.domain.PostBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostBoardRepository extends JpaRepository<PostBoard, Long> {
}
