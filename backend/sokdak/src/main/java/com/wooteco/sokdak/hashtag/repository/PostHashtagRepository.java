package com.wooteco.sokdak.hashtag.repository;

import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

    List<PostHashtag> findAllByPostId(Long id);

    void deleteAllByPostId(Long postId);

    boolean existsByHashtagId(Long id);
}
