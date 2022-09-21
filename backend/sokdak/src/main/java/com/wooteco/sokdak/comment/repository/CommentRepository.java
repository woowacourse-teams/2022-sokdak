package com.wooteco.sokdak.comment.repository;

import com.wooteco.sokdak.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c.nickname FROM Comment c WHERE c.member.id = :memberId and c.post.id = :postId")
    List<String> findNickNamesByPostIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);

    @Query(value = "SELECT c.nickname FROM Comment c WHERE c.post.id = :postId")
    List<String> findNicknamesByPostId(@Param("postId") Long postId);

    List<Comment> findAllByPostIdAndParentId(Long postId, Long parentId);

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    void deleteAllByPostId(Long id);

    @Query(value = "SELECT c FROM Comment c LEFT JOIN FETCH c.commentReports cr LEFT JOIN FETCH cr.reporter WHERE c.id = :commentId")
    Optional<Comment> findByCommentQuery(@Param("commentId") Long commentId);
}
