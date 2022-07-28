package com.wooteco.sokdak.board.service;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.dto.BoardResponse;
import com.wooteco.sokdak.board.dto.BoardsResponse;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.exception.BoardNotFoundException;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
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

    public BoardsResponse findBoards() {
        List<BoardResponse> boardResponses = boardRepository.findAll()
                .stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
        return new BoardsResponse(boardResponses);
    }

    public void savePostBoard(Post savedPost, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        PostBoard postBoard = PostBoard.builder()
                .build();

        postBoard.addPost(savedPost);
        postBoard.addBoard(board);
        postBoardRepository.save(postBoard);
    }
}
