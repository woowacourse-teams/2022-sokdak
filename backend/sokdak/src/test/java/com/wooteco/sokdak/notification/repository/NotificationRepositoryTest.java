package com.wooteco.sokdak.notification.repository;

import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.RepositoryTest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

class NotificationRepositoryTest extends RepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager em;

    private Post post;
    private Comment comment;
    private Member member2;
    private Notification notification;

    @BeforeEach
    void setUp() {
        member2 = Member.builder()
                .username(Username.of(encryptor, "josh"))
                .password(Password.of(encryptor, "Abcd123!@"))
                .nickname(new Nickname("joshNickname"))
                .build();
        memberRepository.save(member2);
        post = Post.builder()
                .member(member1)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(member1.getNickname())
                .build();
        postRepository.save(post);
        comment = Comment.builder()
                .post(post)
                .member(member2)
                .message("댓글")
                .nickname("닉네임")
                .build();
        commentRepository.save(comment);
        notification = Notification.newComment(member1.getId(), post.getId());
        notificationRepository.save(notification);
    }

    @DisplayName("알림들의 id를 받아 조회 여부를 true로 변경한다.")
    @Test
    void inquireNotificationByIds() {
        Long memberId = 1L;
        Long postId = 1L;
        Notification notification2 = Notification.newComment(memberId, postId);
        Notification notification3 = Notification.postReport(memberId, postId);
        notificationRepository.saveAll(List.of(notification2, notification3));
        em.clear();

        notificationRepository.inquireNotificationByIds(
                List.of(notification.getId(), notification2.getId(), notification3.getId()));

        List<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(member1.getId(), Pageable.ofSize(3))
                .getContent();
        boolean actual = notifications.stream()
                .allMatch(Notification::isInquired);
        assertThat(actual).isTrue();
    }

    @DisplayName("회원이 조회하지 않은 알림이 존재하는지 반환")
    @Test
    void existsByMemberIdAndInquired() {
        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(member1.getId());

        assertThat(actual).isTrue();
    }

    @DisplayName("회원에 따른 알림을 반환한다.")
    @Test
    void findNotificationsByMemberId() {
        Long memberId = 1L;
        Notification notification2 = Notification.newComment(memberId, 1L);
        notificationRepository.save(notification2);
        Notification notification3 = Notification.postReport(memberId, 2L);
        notificationRepository.save(notification3);
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("createdAt").descending());

        Slice<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(member1.getId(), pageRequest);

        assertAll(
                () -> assertThat(notifications.isLast()).isFalse(),
                () -> assertThat(notifications.getContent().get(0).getNotificationType()).isEqualTo(POST_REPORT),
                () -> assertThat(notifications.getContent().get(0).getPostId()).isEqualTo(2L),
                () -> assertThat(notifications.getContent().get(1).getNotificationType()).isEqualTo(NEW_COMMENT),
                () -> assertThat(notifications.getContent().get(1).getPostId()).isEqualTo(1L)
        );
    }
}
