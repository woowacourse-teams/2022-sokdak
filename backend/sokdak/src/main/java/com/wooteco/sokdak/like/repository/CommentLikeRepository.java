package com.wooteco.sokdak.like.repository;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.like.domain.CommentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    void deleteAllByCommentId(Long id);

    boolean existsByMemberIdAndComment(Long memberId, Comment comment);

    Optional<CommentLike> findByMemberIdAndComment(Long memberId, Comment comment);
}
