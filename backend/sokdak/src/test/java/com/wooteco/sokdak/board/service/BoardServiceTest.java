package com.wooteco.sokdak.board.service;

import static com.wooteco.sokdak.util.fixture.BoardFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.util.fixture.BoardFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("게시판을 생성한다.")
    @Test
    void test() {
        boardService.createBoard(BOARD_REQUEST);

        Optional<Board> savedBoard = boardRepository.findById(1L);

        assertAll(
                () -> assertThat(savedBoard).isNotNull(),
                () -> assertThat(savedBoard.get().getName()).isEqualTo(BOARD_REQUEST.getName())
        );
    }
}
