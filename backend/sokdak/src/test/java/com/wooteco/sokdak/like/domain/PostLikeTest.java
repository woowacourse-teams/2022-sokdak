package com.wooteco.sokdak.like.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostLikeTest {

    @DisplayName("특정 회원이 누른 게시글 좋아요인지 반환한다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isLikeOf(Long memberId, boolean expected) {
        Post post = Post.builder()
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postLikes(new ArrayList<>())
                .writerNickname(VALID_POST_WRITER_NICKNAME)
                .build();
        Member member = Member.builder()
                .id(1L)
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        PostLike postLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();

        boolean actual = postLike.isLikeOf(memberId);

        assertThat(actual).isEqualTo(expected);
    }
}