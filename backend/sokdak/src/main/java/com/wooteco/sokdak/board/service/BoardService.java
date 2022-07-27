package com.wooteco.sokdak.board.service;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostBoardRepository postBoardRepository;

    public BoardService(BoardRepository boardRepository, PostBoardRepository postBoardRepository) {
        this.boardRepository = boardRepository;
        this.postBoardRepository = postBoardRepository;
    }

    public NewBoardResponse createBoard(NewBoardRequest newBoardRequest) {
        Board board = Board.builder()
                .name(newBoardRequest.getName())
                .build();

        Board savedBoard = boardRepository.save(board);
        return new NewBoardResponse(savedBoard);
    }
}
