package com.wooteco.sokdak.board.service;

import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_1;
import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.dto.BoardsResponse;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.repository.BoardRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql("classpath:truncate.sql")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("게시판을 생성한다.")
    @Test
    void createBoard() {
        NewBoardResponse board = boardService.createBoard(BOARD_REQUEST_1);

        Optional<Board> savedBoard = boardRepository.findById(board.getId());

        assertAll(
                () -> assertThat(savedBoard).isNotNull(),
                () -> assertThat(savedBoard.get().getTitle()).isEqualTo(BOARD_REQUEST_1.getName())
        );
    }

    @DisplayName("게시판 목록을 조회한다.")
    @Test
    void findBoards() {
        boardService.createBoard(BOARD_REQUEST_1);
        boardService.createBoard(BOARD_REQUEST_2);

        BoardsResponse boards = boardService.findBoards();

        assertThat(boards.getBoards()).hasSize(2)
                .extracting("title")
                .containsExactly(BOARD_REQUEST_1.getName(), BOARD_REQUEST_2.getName());
    }
}
