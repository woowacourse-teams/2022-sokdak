package com.wooteco.sokdak.like.repository;

import com.wooteco.sokdak.like.domain.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);

    int countByPostId(Long postId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteAllByPostId(Long id);
}
