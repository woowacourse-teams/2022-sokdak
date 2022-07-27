package com.wooteco.sokdak.hashtag.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.hashtag.repository.PostHashtagRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.post.service.PostService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class HashtagServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    private Post post;
    private Hashtag tag1;
    private Hashtag tag2;

    @BeforeEach
    public void setUp() {
        Member member = memberRepository.findById(1L)
                .orElseThrow(MemberNotFoundException::new);
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        tag1 = Hashtag.builder()
                .name("태그1")
                .build();
        tag2 = Hashtag.builder()
                .name("태그2")
                .build();
    }

    @DisplayName("해시태그가 포함된 게시글 작성 기능")
    @Test
    void addPostWithHashtag() {
        final List<String> expected = List.of("태그1", "태그2");
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문", expected);

        Long postId = postService.addPost(newPostRequest, AUTH_INFO);
        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPostId(postId);

        final List<String> hashtags = postHashtags
                .stream()
                .map(PostHashtag::getHashtag)
                .map(Hashtag::getName)
                .collect(Collectors.toList());
        assertThat(hashtags).isEqualTo(expected);
    }


    @DisplayName("게시글에 해시태그를 추가하는 수정 기능")
    @Test
    void updatePostWithAddingHashtag() {
        Long postId = postRepository.save(post).getId();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("변경된 제목", "변경된 본문", List.of("태그1", "태그2"));

        postService.updatePost(postId, postUpdateRequest, AUTH_INFO);

        List<String> hashtags = postHashtagRepository.findAllByPostId(postId)
                .stream()
                .map(PostHashtag::getHashtag)
                .map(Hashtag::getName)
                .collect(Collectors.toList());
        assertThat(hashtags).containsOnly("태그1", "태그2");
    }

    @DisplayName("게시글에 일부 해시태그를 삭제하는 수정 기능")
    @Test
    void updatePostWithDeletingHashtag() {
        Long postId = savePostWithHashtags(post, List.of(tag1, tag2));

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("변경된 제목", "변경된 본문", List.of("태그1"));
        postService.updatePost(postId, postUpdateRequest, AUTH_INFO);

        List<String> hashtagNames = postHashtagRepository.findAllByPostId(postId)
                .stream()
                .map(PostHashtag::getHashtag)
                .map(Hashtag::getName)
                .collect(Collectors.toList());
        assertThat(hashtagNames).containsOnly("태그1");
    }

    @DisplayName("해시태그가 있는 게시글 삭제 시 더 이상 사용되지 않는 해시태그 삭제 기능")
    @Test
    void deletePostWithHashtag() {
        Long postId = savePostWithHashtags(post, List.of(tag1, tag2));
        Post post = Post.builder()
                .title("제목2").
                        content("내용2")
                .build();
        savePostWithHashtags(post, List.of(tag1));

        postService.deletePost(postId, AUTH_INFO);

        assertAll(
                () -> assertThat(postHashtagRepository.findAllByPostId(postId)).isEmpty(),
                () -> assertThat(hashtagRepository.existsByName("태그1")).isTrue(),
                () -> assertThat(!hashtagRepository.existsByName("태그2")).isTrue()
        );
    }

    private Long savePostWithHashtags(Post post, List<Hashtag> tags) {
        Long postId = postRepository.save(post).getId();
        hashtagRepository.saveAll(tags);
        tags.forEach(tag -> postHashtagRepository.save(PostHashtag.builder().post(post).hashtag(tag).build()));
        return postId;
    }
}
