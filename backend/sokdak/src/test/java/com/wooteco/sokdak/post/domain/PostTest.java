package com.wooteco.sokdak.post.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getMembersForReport;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.report.domain.PostReport;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

class PostTest {

    private Post post;
    private Member member;

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

    @DisplayName("리포트 연관관계 편의 메소드")
    @Test
    void addReport() {
        PostReport postReport = PostReport.builder()
                .post(post)
                .reporter(member)
                .reportMessage("gogo")
                .build();
        assertThat(post.getPostReports()).contains(postReport);
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

    static Stream<Arguments> isAnonymousArgs() {
        return Stream.of(
                Arguments.arguments("익명", true),
                Arguments.arguments(VALID_NICKNAME, false)
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
}
