package com.wooteco.sokdak.board.repository;

import com.wooteco.sokdak.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
