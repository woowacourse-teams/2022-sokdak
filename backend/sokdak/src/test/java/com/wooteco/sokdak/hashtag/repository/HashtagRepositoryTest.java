package com.wooteco.sokdak.hashtag.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(JPAConfig.class)
class HashtagRepositoryTest extends RepositoryTest {

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    private final Hashtag tag1 = Hashtag.builder().name("태그1").build();
    private final Hashtag tag2 = Hashtag.builder().name("태그2").build();

    @BeforeEach
    void setUp() {
        Post post1 = Post.builder()
                .title("제목1")
                .content("본문1")
                .member(member1)
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member1)
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .content("본문3")
                .member(member1)
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
