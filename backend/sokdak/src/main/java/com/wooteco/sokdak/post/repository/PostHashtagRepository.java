package com.wooteco.sokdak.post.repository;

import com.wooteco.sokdak.post.domain.PostHashtag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

    List<PostHashtag> findAllByPostId(Long id);

    void deleteAllByPostId(Long postId);
}
