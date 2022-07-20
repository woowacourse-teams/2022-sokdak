package com.wooteco.sokdak.post.repository;

import com.wooteco.sokdak.post.domain.Hashtag;
import com.wooteco.sokdak.post.domain.PostHashtag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    boolean existsByName(String name);

    Optional<Hashtag> findByName(String name);

}
