package com.wooteco.sokdak.hashtag.repository;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_ENCRYPTED_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JPAConfig.class)
class HashtagRepositoryTest {
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

    @BeforeEach
    void setUp() {
        Member member = memberRepository
                .findByUsernameValueAndPassword(VALID_USERNAME, VALID_ENCRYPTED_PASSWORD)
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

        postHashtagRepository.save(PostHashtag.builder().post(post1).hashtag(tag1).build());
        postHashtagRepository.save(PostHashtag.builder().post(post1).hashtag(tag2).build());
        postHashtagRepository.save(PostHashtag.builder().post(post2).hashtag(tag1).build());
        postHashtagRepository.save(PostHashtag.builder().post(post3).hashtag(tag2).build());
    }

    @Test
    void findAllByNameContains() {
        Hashtags hashtags = new Hashtags(hashtagRepository.findAllByNameContains("태그"));
        assertThat(hashtags.getNames()).containsOnly("태그1", "태그2");
    }
}
