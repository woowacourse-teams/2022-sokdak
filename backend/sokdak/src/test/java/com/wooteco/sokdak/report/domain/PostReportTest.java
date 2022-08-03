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

    @DisplayName("같은 게시글을 같은 신고자가 중복신고하면 예외발생")
    @Test
    void addPost_Exception_AlreadyReport() {
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
