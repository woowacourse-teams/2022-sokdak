package com.wooteco.sokdak.notification.repository;

import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@DataJpaTest
@Import(JPAConfig.class)
class NotificationRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private Member member;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        Member member2 = Member.builder()
                .username("josh")
                .password(VALID_PASSWORD)
                .nickname("joshNickname")
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);
        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(member.getNickname())
                .build();
        postRepository.save(post);
        comment = Comment.builder()
                .post(post)
                .member(member2)
                .message("댓글")
                .nickname("닉네임")
                .build();
        commentRepository.save(comment);
        Notification notification = Notification.builder()
                .member(member)
                .post(post)
                .comment(comment)
                .notificationType(NEW_COMMENT)
                .build();
        notificationRepository.save(notification);
    }

    @DisplayName("회원이 조회하지 않은 알림이 존재하는지 반환")
    @Test
    void existsByMemberIdAndInquired() {
        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(member.getId());

        assertThat(actual).isTrue();
    }

    @DisplayName("댓글 알림을 삭제한다.")
    @Test
    void deleteAllByCommentId() {
        notificationRepository.deleteAllByCommentId(comment.getId());

        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(member.getId());

        assertThat(actual).isFalse();
    }

    @DisplayName("회원에 따른 알림을 반환한다.")
    @Test
    void findNotificationsByMemberId() {
        Member member3 = Member.builder()
                .username("east")
                .password(VALID_PASSWORD)
                .nickname("eastNickname")
                .build();
        memberRepository.save(member3);
        Comment comment2 = Comment.builder()
                .post(post)
                .member(member3)
                .message("댓글2")
                .nickname("깔깔")
                .build();
        commentRepository.save(comment2);
        Notification notification2 = Notification.builder()
                .member(member)
                .post(post)
                .comment(comment2)
                .notificationType(NEW_COMMENT)
                .build();
        notificationRepository.save(notification2);
        Notification notification3 = Notification.builder()
                .member(member)
                .post(post)
                .notificationType(POST_REPORT)
                .build();
        notificationRepository.save(notification3);
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("createdAt").descending());

        Slice<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(member.getId(), pageRequest);

        assertAll(
                () -> assertThat(notifications.isLast()).isFalse(),
                () -> assertThat(notifications.getContent().get(0).getNotificationType()).isEqualTo(POST_REPORT),
                () -> assertThat(notifications.getContent().get(0).getContent()).isEqualTo(post.getTitle()),
                () -> assertThat(notifications.getContent().get(1).getNotificationType()).isEqualTo(NEW_COMMENT),
                () -> assertThat(notifications.getContent().get(1).getContent()).isEqualTo(post.getTitle())
        );
    }
}
