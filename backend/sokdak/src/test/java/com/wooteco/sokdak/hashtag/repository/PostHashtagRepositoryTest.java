package com.wooteco.sokdak.hashtag.repository;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_ENCRYPTED_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.RepositoryTest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@Import(JPAConfig.class)
class PostHashtagRepositoryTest extends RepositoryTest {

    @Autowired
    private PostHashtagRepository postHashtagRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private HashtagRepository hashtagRepository;

    private final Hashtag tag1 = Hashtag.builder().name("태그1").build();
    private final Hashtag tag2 = Hashtag.builder().name("태그2").build();
    private final Hashtag tag3 = Hashtag.builder().name("태그3").build();

    @BeforeEach
    void setUp() {
        member = memberRepository
                .findByUsernameAndPassword(VALID_USERNAME, VALID_ENCRYPTED_PASSWORD)
                .orElseThrow();

        Post post1 = Post.builder()
                .title("제목1")
                .content("본문1")
                .member(member)
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member)
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .content("본문3")
                .member(member)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        hashtagRepository.save(tag1);
        hashtagRepository.save(tag2);
        hashtagRepository.save(tag3);

        postHashtagRepository.save(PostHashtag.builder().post(post1).hashtag(tag1).build());
        postHashtagRepository.save(PostHashtag.builder().post(post1).hashtag(tag2).build());
        postHashtagRepository.save(PostHashtag.builder().post(post1).hashtag(tag3).build());
        postHashtagRepository.save(PostHashtag.builder().post(post2).hashtag(tag1).build());
        postHashtagRepository.save(PostHashtag.builder().post(post2).hashtag(tag3).build());
        postHashtagRepository.save(PostHashtag.builder().post(post3).hashtag(tag1).build());
    }

    @Test
    void countByHashtagId() {
        int count = postHashtagRepository.countByHashtagId(tag1.getId());
        assertThat(count).isEqualTo(3);
    }

    @Test
    void findAllByHashtagId() {
        Pageable pageable = PageRequest.of(0, 3, DESC, "createdAt");

        Slice<Post> posts = postHashtagRepository.findAllPostByHashtagId(tag1.getId(), pageable);
        assertThat(posts.getNumberOfElements()).isEqualTo(3);
    }

    @Test
    void findAllByHashtagOrderByCount() {
        List<Hashtag> hashtags = hashtagRepository.findAllByNameContains("태그");
        PageRequest pageable = PageRequest.of(0, 5);
        List<Tuple> allByHashtagOrderByCount = postHashtagRepository.findAllByHashtagOrderByCount(hashtags, pageable);
        List<String> names = new ArrayList<>();

        for (Tuple tuple : allByHashtagOrderByCount) {
            names.add(tuple.get(0, Hashtag.class).getName());
        }

        assertThat(names).containsExactly("태그1", "태그3", "태그2");
    }
}
