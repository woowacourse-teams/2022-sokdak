package com.wooteco.sokdak.post.domain;

import static com.wooteco.sokdak.board.domain.BoardType.NON_WRITABLE;
import static com.wooteco.sokdak.board.domain.BoardType.WRITABLE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.ENCRYPTOR;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME_TEXT;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getMembersForReport;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import com.wooteco.sokdak.report.domain.PostReport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;


class PostTest {

    private Post post;
    private Member member;
    private static final EncryptorI encryptor = ENCRYPTOR;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        post = Post.builder()
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postLikes(new ArrayList<>())
                .writerNickname(VALID_POST_WRITER_NICKNAME)
                .member(member)
                .build();
    }

    @DisplayName("게시글 변경되지 않았으면 False 반환")
    @Test
    void isModified_False() {
        LocalDateTime now = LocalDateTime.now();
        ReflectionTestUtils.setField(post, "createdAt", now);
        ReflectionTestUtils.setField(post, "modifiedAt", now);

        assertThat(post.isModified()).isFalse();
    }

    @DisplayName("게시글 변경되었으면 true 반환")
    @Test
    void isModified_True() {
        LocalDateTime futureTime = LocalDateTime.of(3333, 1, 10, 3, 3);
        ReflectionTestUtils.setField(post, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(post, "modifiedAt", futureTime);

        assertThat(post.isModified()).isTrue();
    }

    @DisplayName("게시글 제목 수정")
    @Test
    void updateTitle() {
        post.updateTitle("변경된 제목");

        assertThat(post.getTitle()).isEqualTo("변경된 제목");
    }

    @DisplayName("게시글 본문 수정")
    @Test
    void updateContent() {
        post.updateContent("변경된 본문");

        assertThat(post.getContent()).isEqualTo("변경된 본문");
    }

    @DisplayName("게시글 이미지 이름 수정")
    @Test
    void updateImageName() {
        post.updateImageName("변경된 이미지 이름");

        assertThat(post.getImageName()).isEqualTo("변경된 이미지 이름");
    }

    @DisplayName("게시글이 등록된 보드 찾기")
    @Test
    void getWritableBoard() {
        Board board = Board.builder()
                .name("자유게시판")
                .boardType(WRITABLE)
                .build();
        Board adminBoard = Board.builder()
                .name("핫게")
                .boardType(NON_WRITABLE)
                .build();
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addPost(post);
        postBoard.addBoard(board);
        PostBoard adminPostBoard = PostBoard.builder().build();
        adminPostBoard.addPost(post);
        adminPostBoard.addBoard(adminBoard);

        ReflectionTestUtils.setField(post, "postBoards", List.of(postBoard, adminPostBoard));

        assertThat(post.getWritableBoard()).isEqualTo(board);
    }

    @DisplayName("신고가 5개 이상이면 isBlocked()가 true를 반환하고, 게시글의 정보는 반환되지 않는다.")
    @Test
    void isBlocked_true() {
        List<Member> members = getMembersForReport();
        int blockCondition = 5;
        for (int i = 0; i < blockCondition; ++i) {
            PostReport.builder()
                    .post(post)
                    .reporter(members.get(i))
                    .reportMessage("신고")
                    .build();
        }

        assertAll(
                () -> assertThat(post.isBlocked()).isTrue(),
                () -> assertThat(post.getNickname()).isEqualTo("블라인드 처리된 게시글입니다."),
                () -> assertThat(post.getTitle()).isEqualTo("블라인드 처리된 게시글입니다."),
                () -> assertThat(post.getContent()).isEqualTo("블라인드 처리된 게시글입니다.")
        );
    }

    @DisplayName("익명여부 확인")
    @ParameterizedTest
    @MethodSource("isAnonymousArgs")
    void isAnonymous(String writerNickname, boolean result) {
        Post anonymousPost = Post.builder()
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(writerNickname)
                .member(member)
                .build();
        assertThat(anonymousPost.isAnonymous()).isEqualTo(result);
    }

    private static Stream<Arguments> isAnonymousArgs() {
        return Stream.of(
                Arguments.arguments("익명", true),
                Arguments.arguments(VALID_NICKNAME_TEXT, false)
        );
    }

    @DisplayName("신고가 5개 미만이면 isBlocked()가 false를 반환하고 게시글 정보들이 반환된다.")
    @Test
    void isBlocked_false() {
        List<Member> members = getMembersForReport();
        int unblockCondition = 4;
        for (int i = 0; i < unblockCondition; ++i) {
            PostReport.builder()
                    .post(post)
                    .reporter(members.get(i))
                    .reportMessage("신고")
                    .build();
        }

        assertAll(
                () -> assertThat(post.isBlocked()).isFalse(),
                () -> assertThat(post.getNickname()).isEqualTo(VALID_POST_WRITER_NICKNAME),
                () -> assertThat(post.getTitle()).isEqualTo(VALID_POST_TITLE),
                () -> assertThat(post.getContent()).isEqualTo(VALID_POST_CONTENT)
        );
    }

    @DisplayName("게시글의 회원 정보가 일치하는지 반환")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isOwner(Long accessMemberId, boolean expected) {
        boolean actual = post.isOwner(accessMemberId);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 멤버가 신고를 이미 했으면 true를, 안했으면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("hasReportByMemberArguments")
    void hasReportByMember(Member reporter, Member member, boolean expected) {
        PostReport postReport = PostReport.builder()
                .post(post)
                .reporter(reporter)
                .reportMessage("report")
                .build();
        post.addReport(postReport);

        assertThat(post.hasReportByMember(member)).isEqualTo(expected);
    }

    @DisplayName("게시글에 특정 멤버가 좋아요를 눌렀는지 반환한다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void hasLikeOfMember(Long memberId, boolean expected) {
        PostLike.builder()
                .post(post)
                .member(member)
                .build();
        boolean actual = post.hasLikeOfMember(memberId);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게시글에 특정 멤버의 좋아요를 삭제한다.")
    @Test
    void deleteLikeOfMember() {
        PostLike.builder()
                .post(post)
                .member(member)
                .build();

        post.deleteLikeOfMember(member.getId());

        assertThat(post.hasLikeOfMember(member.getId())).isFalse();
    }

    @DisplayName("핫게시판을 제외한 게시글이 속한 boardId를 가져온다.")
    @Test
    void getBoardId() {
        Board hotBoard = mock(Board.class);
        Board freeBoard = mock(Board.class);
        when(hotBoard.getId()).thenReturn(1L);
        when(freeBoard.getId()).thenReturn(2L);
        PostBoard hotPostBoard = PostBoard.builder().build();
        hotPostBoard.addPost(post);
        hotPostBoard.addBoard(hotBoard);
        PostBoard freePostBoard = PostBoard.builder().build();
        freePostBoard.addPost(post);
        freePostBoard.addBoard(freeBoard);

        assertThat(post.getBoardId()).isEqualTo(2L);
    }

    static Stream<Arguments> hasReportByMemberArguments() {
        Member reporter = Member.builder()
                .username(Username.of(encryptor, "reporter"))
                .nickname(new Nickname("reporterNickname"))
                .password(Password.of(encryptor, "Abcd123!@"))
                .build();
        Member member = Member.builder()
                .username(Username.of(encryptor, "member"))
                .nickname(new Nickname("memberNickname"))
                .password(Password.of(encryptor, "Abcd123!@"))
                .build();
        return Stream.of(
                Arguments.of(reporter, reporter, true),
                Arguments.of(reporter, member, false)
        );
    }

    @DisplayName("글의 조회수를 1 증가시킨다.")
    @Test
    void addViewCount() {
        int viewCount = post.getViewCount();
        post.addViewCount();

        assertThat(viewCount + 1).isEqualTo(post.getViewCount());
    }
}
