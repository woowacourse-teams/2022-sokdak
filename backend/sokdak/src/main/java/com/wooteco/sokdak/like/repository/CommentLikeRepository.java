package com.wooteco.sokdak.like.repository;

import com.wooteco.sokdak.like.domain.CommentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId);

    int countByCommentId(Long commentId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    void deleteAllByCommentId(Long id);
}
