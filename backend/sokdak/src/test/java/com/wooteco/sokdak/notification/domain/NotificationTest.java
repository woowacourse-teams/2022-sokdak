package com.wooteco.sokdak.notification.domain;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_REPLY;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationTest {

    private static final Member MEMBER1 = Member.builder()
            .id(1L)
            .nickname(VALID_NICKNAME)
            .username(VALID_USERNAME)
            .password(VALID_PASSWORD)
            .build();
    private static final Member MEMBER2 = Member.builder()
            .id(1L)
            .nickname("joshNickname")
            .username("josh")
            .password("Ajkl312@!")
            .build();
    private static final Post POST = Post.builder()
            .title(VALID_POST_TITLE)
            .content(VALID_POST_CONTENT)
            .member(MEMBER1)
            .build();
    private static final Comment COMMENT = Comment.builder()
            .member(MEMBER2)
            .post(POST)
            .message("hi~")
            .build();
    private static final Comment REPLY = Comment.builder()
            .member(MEMBER1)
            .post(POST)
            .message("hey~")
            .build();

    @DisplayName("게시글에 새로운 댓글이 달렸을 시에, 알림 내용은 게시글 제목이 반환된다.")
    @Test
    void getContent_NewComment() {
        Notification notification = Notification.builder()
                .member(MEMBER1)
                .post(POST)
                .notificationType(NEW_COMMENT)
                .build();

        String actual = notification.getContent();

        assertThat(actual).isEqualTo(POST.getTitle());
    }

    @DisplayName("게시글이 Hot 게시판에 등록되었을 시, 알림 내용은 게시글 제목이 반환된다.")
    @Test
    void getContent_HotBoard() {
        Notification notification = Notification.builder()
                .member(MEMBER1)
                .post(POST)
                .notificationType(HOT_BOARD)
                .build();

        String actual = notification.getContent();

        assertThat(actual).isEqualTo(POST.getTitle());
    }

    @DisplayName("게시글이 일정 횟수 이상 신고 되었을 시, 알림 내용은 게시글 제목이 반환된다.")
    @Test
    void getContent_PostReport() {
        Notification notification = Notification.builder()
                .member(MEMBER1)
                .post(POST)
                .notificationType(POST_REPORT)
                .build();

        String actual = notification.getContent();

        assertThat(actual).isEqualTo(POST.getTitle());
    }

    @DisplayName("댓글이 일정 횟수 이상 신고 되었을 시, 알림 내용은 댓글 내용이 반환된다.")
    @Test
    void getContent_CommentReport() {
        Notification notification = Notification.builder()
                .member(MEMBER1)
                .post(POST)
                .comment(COMMENT)
                .notificationType(COMMENT_REPORT)
                .build();

        String actual = notification.getContent();

        assertThat(actual).isEqualTo(COMMENT.getMessage());
    }

    @DisplayName("댓글에 답글이 달렸을 시에, 알림 내용은 댓글 내용이 반환된다.")
    @Test
    void getContent_NewReply() {
        Notification notification = Notification.builder()
                .member(MEMBER1)
                .post(POST)
                .comment(COMMENT)
                .notificationType(NEW_REPLY)
                .build();

        String actual = notification.getContent();

        assertThat(actual).isEqualTo(COMMENT.getMessage());
    }

    @DisplayName("답글이 신고되었을 시에, 알림 내용은 답글 내용이 반환된다.")
    @Test
    void getContent_ReplyReport() {
        Notification notification = Notification.builder()
                .member(MEMBER1)
                .post(POST)
                .comment(REPLY)
                .notificationType(NEW_REPLY)
                .build();

        String actual = notification.getContent();

        assertThat(actual).isEqualTo(REPLY.getMessage());
    }

}