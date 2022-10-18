package com.wooteco.sokdak.board.service;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.dto.BoardContentElement;
import com.wooteco.sokdak.board.dto.BoardContentResponse;
import com.wooteco.sokdak.board.dto.BoardResponse;
import com.wooteco.sokdak.board.dto.BoardsResponse;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.exception.BoardNotFoundException;
import com.wooteco.sokdak.board.exception.BoardNotWritableException;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoardService {

    private static final int PAGE_SIZE = 3;
    private static final long APPLICANT_BOARD_ID = 5;

    private final BoardRepository boardRepository;
    private final PostBoardRepository postBoardRepository;
    private final NotificationService notificationService;

    public BoardService(BoardRepository boardRepository, PostBoardRepository postBoardRepository,
                        NotificationService notificationService) {
        this.boardRepository = boardRepository;
        this.postBoardRepository = postBoardRepository;
        this.notificationService = notificationService;
    }

    @Transactional
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

    @Transactional
    public void savePostBoard(Post savedPost, Long boardId, String role) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        validateUserWritableBoard(board, role);

        PostBoard postBoard = PostBoard.builder()
                .build();

        postBoard.addPost(savedPost);
        postBoard.addBoard(board);
        postBoardRepository.save(postBoard);
    }

    @Transactional
    public void checkAndSaveInSpecialBoard(Post originalPost) {
        if (isApplicantBoard(originalPost)) {
            return;
        }
        Board specialBoard = boardRepository.findByTitle("Hot 게시판")
                .orElseThrow(BoardNotFoundException::new);

        Optional<PostBoard> postBoardByPostAndBoard = postBoardRepository
                .findPostBoardByPostAndBoard(originalPost, specialBoard);

        if (postBoardByPostAndBoard.isEmpty()) {
            PostBoard postBoard = PostBoard.builder()
                    .build();

            postBoard.addPost(originalPost);
            postBoard.addBoard(specialBoard);
            postBoardRepository.save(postBoard);
            notificationService.notifyHotBoard(originalPost);
        }
    }

    private boolean isApplicantBoard(Post originalPost) {
        return originalPost.getBoardId().equals(APPLICANT_BOARD_ID);
    }

    private void validateUserWritableBoard(Board board, String role) {
        if (!board.isUserWritable(role)) {
            throw new BoardNotWritableException();
        }
    }

    public BoardContentResponse findBoardsContent() {
        List<BoardContentElement> boardContentElements = new ArrayList<>();

        List<Board> boards = boardRepository.findAll();
        PageRequest page = PageRequest.of(0, PAGE_SIZE, Sort.by("createdAt").descending());

        for (Board board : boards) {
            List<PostsElementResponse> postsElementResponses = findPostsByBoard(board.getId(), page).getPosts();
            BoardContentElement boardContentElement = BoardContentElement.from(board, postsElementResponses);
            boardContentElements.add(boardContentElement);
        }
        return BoardContentResponse.of(boardContentElements);
    }

    public PostsResponse findPostsByBoard(Long boardId, Pageable pageable) {
        Slice<Post> posts = postBoardRepository.findPostsByBoardId(boardId, pageable);
        return PostsResponse.ofPostSlice(posts);
    }
}
