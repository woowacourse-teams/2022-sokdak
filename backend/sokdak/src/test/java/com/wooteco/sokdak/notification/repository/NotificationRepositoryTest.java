package com.wooteco.sokdak.notification.repository;

import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_ENCRYPTED_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JPAConfig.class)
class NotificationRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @DisplayName("")
    @Test
    void existsByMemberIdAndInquired() {
        Member member = Member.builder()
                .username(VALID_USERNAME)
                .password(VALID_ENCRYPTED_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        memberRepository.save(member);
        Post post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(member.getNickname())
                .build();
        postRepository.save(post);
        Notification notification = Notification.builder()
                .member(member)
                .post(post)
                .content("댓글입니다.")
                .notificationType(NEW_COMMENT)
                .build();

        notificationRepository.save(notification);

        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(member.getId());

        assertThat(actual).isTrue();
    }
}
