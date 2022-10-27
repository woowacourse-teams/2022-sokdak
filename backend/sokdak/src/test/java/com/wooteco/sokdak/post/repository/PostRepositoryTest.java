package com.wooteco.sokdak.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.util.RepositoryTest;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@Import(JPAConfig.class)
class PostRepositoryTest extends RepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager em;

    private Post post1;
    private Post post2;
    private Post post3;
    private Post post4;
    private Post post5;

    @BeforeEach
    void setUp() {
        post1 = Post.builder()
                .title("제목1")
                .content("본문1")
                .member(member1)
                .build();
        post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member1)
                .build();
        post3 = Post.builder()
                .title("제목3")
                .content("본문3")
                .member(member1)
                .build();
        post4 = Post.builder()
                .title("제목4")
                .content("본문4")
                .member(member1)
                .build();
        post5 = Post.builder()
                .title("제목5")
                .content("본문5")
                .member(member1)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
    }

    @DisplayName("게시글, 회원 매핑 확인")
    @Test
    void findPostWithMember() {
        Post foundPost = postRepository.findById(post1.getId())
                .orElseThrow();

        assertThat(foundPost.getMember()).isNotNull();
    }

    @DisplayName("특점 멤버의 글을 시간순으로 가져오는지 확인")
    @Test
    void findPostsByMember() {
        Page<Post> result = postRepository.findPostsByMemberOrderByCreatedAtDesc(
                PageRequest.of(0, 2, DESC, "createdAt"), member1);

        assertAll(
                () -> assertThat(result.getContent()).containsExactly(post5, post4),
                () -> assertThat(result.getTotalPages()).isEqualTo(3)
        );
    }

    @DisplayName("post의 viewCount를 1 증가시킨다.")
    @Test
    void updateViewCount() {
        int originViewCount = postRepository.findById(post1.getId()).get().getViewCount();
        postRepository.updateViewCount(post1.getId());
        em.clear();
        int viewCount = postRepository.findById(post1.getId()).get().getViewCount();

        assertThat(originViewCount + 1).isEqualTo(viewCount);
    }

    @DisplayName("특정 쿼리에 부합하는 글을 시간순으로 가져오는지 확인")
    @Test
    void findPostSlicePagesByQuery() {
        Slice<Post> result = postRepository.findPostSlicePagesByQuery(PageRequest.of(0, 2, DESC, "created_at"), "");
        assertThat(result.getContent()).containsExactly(post5, post4);
        assertThat(result.isLast()).isEqualTo(false);
    }

    @DisplayName("특정 쿼리에 부합하는 글의 개수 확인")
    @Test
    void countPostsByQuery() {
        Page<Post> result = postRepository.findPostPagesByQuery(PageRequest.of(0, 2, DESC, "created_at"), "");
        assertThat(result.getTotalElements()).isEqualTo(5L);
    }

    @DisplayName("좋아요 개수를 1 증가한다.")
    @Test
    void increaseLikeCount() {
        int originLikeCount = post1.getLikeCount();

        postRepository.increaseLikeCount(post1.getId());

        Post post = postRepository.findById(post1.getId()).orElseThrow();
        assertThat(post.getLikeCount() - originLikeCount).isEqualTo(1);
    }

    @DisplayName("좋아요 개수를 1 감소한다.")
    @Test
    void decreaseLikeCount() {
        int originLikeCount = post1.getLikeCount();

        postRepository.increaseLikeCount(post1.getId());

        Post post = postRepository.findById(post1.getId()).orElseThrow();
        assertThat(post.getLikeCount() - originLikeCount).isEqualTo(1);

    }
}
