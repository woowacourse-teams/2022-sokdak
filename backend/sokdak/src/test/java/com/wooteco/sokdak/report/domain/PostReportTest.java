package com.wooteco.sokdak.report.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.*;
import static com.wooteco.sokdak.util.fixture.PostFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
import com.wooteco.sokdak.util.fixture.MemberFixture;
import com.wooteco.sokdak.util.fixture.PostFixture;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class PostReportTest {

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postHashtags(Collections.emptyList())
                .build();
    }

    @DisplayName("신고자와 멤버가 같으면 true를 반환한다.")
    @Test
    void isSameReporter() {
        PostReport.builder()
                .post(post)
                .reporter(member)
                .reportMessage("신고")
                .build();

        assertThatThrownBy(() -> PostReport.builder()
                .post(post)
                .reporter(member)
                .reportMessage("신고")
                .build())
                .isInstanceOf(AlreadyReportPostException.class);
    }
}
