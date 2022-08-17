package com.wooteco.sokdak.hashtag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.hashtag.dto.HashtagSearchElementResponse;
import com.wooteco.sokdak.hashtag.dto.HashtagsSearchRequest;
import com.wooteco.sokdak.hashtag.dto.HashtagsSearchResponse;
import com.wooteco.sokdak.hashtag.exception.HashtagNotFoundException;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.hashtag.repository.PostHashtagRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.post.service.PostService;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class HashtagServiceTest extends ServiceTest {

    public static final long WRITABLE_BOARD_ID = 2L;

    @Autowired
    private PostService postService;

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    private Post post;
    private Post post2;
    private Post post3;
    private Hashtag tag1;
    private Hashtag tag2;

    @BeforeEach
    public void setUp() {
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        post2 = Post.builder()
                .title("제목2")
                .content("내용")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        post3 = Post.builder()
                .title("제목3")
                .content("내용")
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
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문", false, List.of("태그1", "태그2"));

        Long postId = postService.addPost(WRITABLE_BOARD_ID, newPostRequest, AUTH_INFO);

        final List<String> hashtags = postHashtagRepository.findAllByPostId(postId)
                .stream()
                .map(PostHashtag::getHashtag)
                .map(Hashtag::getName)
                .collect(Collectors.toList());
        assertThat(hashtags).isEqualTo(List.of("태그1", "태그2"));
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
        savePostWithHashtags(post2, List.of(tag1));

        postService.deletePost(postId, AUTH_INFO);

        assertAll(
                () -> assertThat(postHashtagRepository.findAllByPostId(postId)).isEmpty(),
                () -> assertThat(hashtagRepository.existsByName("태그1")).isTrue(),
                () -> assertThat(!hashtagRepository.existsByName("태그2")).isTrue()
        );
    }

    @DisplayName("특정 해시태그로 검색 시 해시태그가 포함된 게시글 목록을 반환하는 조회 기능")
    @Test
    void findByHashtag() {
        savePostWithHashtags(post, List.of(tag1, tag2));
        savePostWithHashtags(post2, List.of(tag1));
        savePostWithHashtags(post3, List.of(tag2));

        Pageable pageable = PageRequest.of(0, 3, DESC, "createdAt");

        PostsResponse postsResponse = hashtagService.findPostsWithHashtag(tag1.getName(), pageable);

        List<PostsElementResponse> posts = postsResponse.getPosts();
        assertAll(
                () -> assertThat(posts).hasSize(2),
                () -> assertThat(posts).usingRecursiveComparison()
                        .comparingOnlyFields("title")
                        .isEqualTo(List.of(PostsElementResponse.builder().title("제목2"),
                                PostsElementResponse.builder().title("제목")))
        );
    }

    @DisplayName("특정 해시태그로 검색 시 해당 해시태그가 없을 시 예외처리")
    @Test
    void findByHashtag_Exception_NoResult() {
        savePostWithHashtags(post, List.of(tag1));

        Pageable pageable = PageRequest.of(0, 3, DESC, "createdAt");
        assertThatThrownBy(() -> hashtagService.findPostsWithHashtag("없는태그", pageable))
                .isInstanceOf(HashtagNotFoundException.class)
                .hasMessage("해당 이름의 해시태그를 찾을 수 없습니다.");
    }

    @DisplayName("특정 해시태그로 검색 시 해당 페이지의 결과가 없을 시 빈 배열을 반환")
    @Test
    void findByHashtag_Exception_NoPage() {
        savePostWithHashtags(post, List.of(tag1));

        Pageable pageable = PageRequest.of(10, 3, DESC, "createdAt");
        PostsResponse postsResponse = hashtagService.findPostsWithHashtag(tag1.getName(), pageable);

        List<PostsElementResponse> posts = postsResponse.getPosts();
        assertThat(posts).hasSize(0);
    }

    @DisplayName("특정 키워드로 검색 시 해당 키워드가 이름에 포함된 해시태그들을 조회하는 기능")
    @Test
    void findHashtagsWithTagName() {
        savePostWithHashtags(post, List.of(tag1, tag2));
        savePostWithHashtags(post2, List.of(tag1));
        List<HashtagSearchElementResponse> expectedHashtags = List.of(
                HashtagSearchElementResponse.builder().name(tag1.getName()).count(2L).build(),
                HashtagSearchElementResponse.builder().name(tag2.getName()).count(1L).build());

        HashtagsSearchResponse hashtagsSearchResponse = hashtagService.findHashtagsWithTagName(
                new HashtagsSearchRequest("태그", 3));

        List<HashtagSearchElementResponse> responseHashtags = hashtagsSearchResponse.getHashtags();

        assertAll(
                () -> assertThat(responseHashtags).hasSize(2),
                () -> assertThat(responseHashtags).usingRecursiveComparison()
                        .comparingOnlyFields("name", "count")
                        .isEqualTo(expectedHashtags)
        );
    }

    @DisplayName("특정 키워드로 검색 시 해당 키워드가 이름에 포함된 해시태그가 없을 시 결과 값이 비어있다.")
    @Test
    void findHashtagsWithTagName_Exception_NoHashtagName() {
        HashtagsSearchResponse hashtagsSearchResponse = hashtagService.findHashtagsWithTagName(
                new HashtagsSearchRequest("태그", 3));

        assertThat(hashtagsSearchResponse.getHashtags()).isEmpty();
    }

    @DisplayName("특정 키워드로 검색 시 결과 개수 설정이 0 이하일 시 결과 값이 비어있다.")
    @Test
    void findHashtagsWithTagName_Exception_InvalidLimit() {
        HashtagsSearchResponse hashtagsSearchResponse = hashtagService.findHashtagsWithTagName(
                new HashtagsSearchRequest("태그", 0));

        assertThat(hashtagsSearchResponse.getHashtags()).isEmpty();
    }

    private Long savePostWithHashtags(Post post, List<Hashtag> tags) {
        NewPostRequest newPostRequest = new NewPostRequest(
                post.getTitle(), post.getContent(), false, new Hashtags(tags).getNames());
        return postService.addPost(WRITABLE_BOARD_ID, newPostRequest, AUTH_INFO);
    }
}
