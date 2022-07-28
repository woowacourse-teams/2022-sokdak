package com.wooteco.sokdak.hashtag.repository;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    boolean existsByName(String name);

    Optional<Hashtag> findByName(String name);
}
