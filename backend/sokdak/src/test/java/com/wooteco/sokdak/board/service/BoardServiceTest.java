package com.wooteco.sokdak.board.service;

import static com.wooteco.sokdak.board.domain.BoardType.NON_WRITABLE;
import static com.wooteco.sokdak.board.domain.BoardType.WRITABLE;
import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_1;
import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_2;
import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_3;
import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.dto.BoardsResponse;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.exception.BoardNotFoundException;
import com.wooteco.sokdak.board.exception.BoardNotWritableException;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BoardServiceTest extends ServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostBoardRepository postBoardRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private Post post;

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
        BoardsResponse boards = boardService.findBoards();

        assertThat(boards.getBoards()).hasSize(4)
                .extracting("title")
                .containsExactly(BOARD_REQUEST_1.getName(), BOARD_REQUEST_2.getName(), BOARD_REQUEST_3.getName(),
                        BOARD_REQUEST_4.getName());
    }

    @DisplayName("작성 가능 게시판에서 허용된 사용자가 게시글을 쓸 수 있다.")
    @Test
    void savePostBoard() {
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        postRepository.save(post);
        Board board = Board.builder()
                .name("자유게시판")
                .boardType(WRITABLE)
                .build();
        Board savedBoard = boardRepository.save(board);

        boardService.savePostBoard(post, savedBoard.getId(), RoleType.USER.getName());
        Optional<PostBoard> postBoard = postBoardRepository.findPostBoardByPostAndBoard(post, board);

        assertAll(
                () -> assertThat(postBoard).isNotEmpty(),
                () -> assertThat(postBoard.get().getBoard().getTitle()).isEqualTo("자유게시판"),
                () -> assertThat(postBoard.get().getPost().getTitle()).isEqualTo("제목")
        );
    }

    @DisplayName("작성 불가능 게시판에서 허용되지 않은 사용자가 게시글을 쓰면 예외가 발생한다.")
    @Test
    void savePostBoard_Exception() {
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        postRepository.save(post);
        Board board = Board.builder()
                .name("Hot 게시판")
                .boardType(NON_WRITABLE)
                .build();
        Board savedBoard = boardRepository.save(board);

        assertThatThrownBy(() -> boardService.savePostBoard(post, savedBoard.getId(), RoleType.USER.getName()))
                .isInstanceOf(BoardNotWritableException.class);
    }

    @DisplayName("핫게시판에 게시글이 저장될 수 있다.")
    @Test
    void saveInSpecialBoard() {
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        postRepository.save(post);
        Board board = boardRepository.findByTitle("Hot 게시판").get();

        boardService.checkAndSaveInSpecialBoard(post);
        Optional<PostBoard> postBoard = postBoardRepository.findPostBoardByPostAndBoard(post, board);
        boolean newNotification = notificationRepository.existsByMemberIdAndInquiredIsFalse(member.getId());

        assertAll(
                () -> assertThat(postBoard).isNotEmpty(),
                () -> assertThat(postBoard.get().getBoard().getTitle()).isEqualTo("Hot 게시판"),
                () -> assertThat(postBoard.get().getPost().getTitle()).isEqualTo("제목"),
                () -> assertThat(newNotification).isTrue()
        );
    }

    @DisplayName("게시글을 존재하지 않는 게시판에 작성하면 예외가 발생한다.")
    @Test
    void savePostBoard_Exception_NoBoard() {
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        postRepository.save(post);

        assertThatThrownBy(() -> boardService.savePostBoard(post, 9999L, RoleType.USER.getName()))
                .isInstanceOf(BoardNotFoundException.class);
    }
}
