package com.wooteco.sokdak.board.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.support.token.Login;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(@Valid @RequestBody NewBoardRequest newBoardRequest,
                                            @Login AuthInfo authInfo) {
        NewBoardResponse board = boardService.createBoard(newBoardRequest);
        return ResponseEntity.created(URI.create("/posts/" + board.getId())).build();
    }
}
