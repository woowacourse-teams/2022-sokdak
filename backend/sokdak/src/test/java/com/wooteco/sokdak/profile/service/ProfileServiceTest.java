package com.wooteco.sokdak.profile.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.InvalidNicknameException;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.profile.dto.MyPostsResponse;
import com.wooteco.sokdak.profile.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.profile.exception.DuplicateNicknameException;
import com.wooteco.sokdak.util.IntegrationTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class ProfileServiceTest extends IntegrationTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private static final int WRONG_PAGE = 99;

    @DisplayName("내가 쓴 글 조회 기능")
    @Test
    void findMyPosts() {
        Member member = memberRepository.findById(AUTH_INFO.getId())
                .orElseThrow(MemberNotFoundException::new);
        Post post1 = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .build();
        postRepository.save(post1);
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member)
                .build();
        postRepository.save(post2);

        MyPostsResponse myPosts = profileService.findMyPosts(PageRequest.of(0, 1), AUTH_INFO);

        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostsElementResponse.from(post2))),
                () -> assertThat(myPosts.getTotalPageCount()).isEqualTo(2)
        );
    }

    @DisplayName("없는 페이지로 요청이 올 시 빈 배열 반환")
    @Test
    void findMyPosts_Exception_WrongPage() {
        Member member = memberRepository.findById(AUTH_INFO.getId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .writerNickname(member.getNickname())
                .member(member)
                .likes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        postRepository.save(post);

        MyPostsResponse myPosts = profileService.findMyPosts(PageRequest.of(WRONG_PAGE, 3), AUTH_INFO);

        assertAll(
                () -> assertThat(myPosts.getPosts()).hasSize(0),
                () -> assertThat(myPosts.getTotalPageCount()).isEqualTo(1)
        );
    }
}
