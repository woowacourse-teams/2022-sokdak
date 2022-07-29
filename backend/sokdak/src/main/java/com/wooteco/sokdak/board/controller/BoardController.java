package com.wooteco.sokdak.board.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.board.dto.BoardContentResponse;
import com.wooteco.sokdak.board.dto.BoardsResponse;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.support.token.Login;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        return ResponseEntity.created(URI.create("/boards/" + board.getId())).build();
    }

    @GetMapping
    public ResponseEntity<BoardsResponse> findBoards() {
        BoardsResponse boardsResponse = boardService.findBoards();
        return ResponseEntity.ok(boardsResponse);
    }

    @GetMapping("/content")
    public ResponseEntity<BoardContentResponse> findBoardsContent() {
        BoardContentResponse boardContentResponse = boardService.findBoardsContent();
        return ResponseEntity.ok(boardContentResponse);
    }
}
