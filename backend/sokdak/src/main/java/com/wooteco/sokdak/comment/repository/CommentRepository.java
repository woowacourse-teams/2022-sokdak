package com.wooteco.sokdak.comment.repository;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c.nickname FROM Comment c WHERE c.post = :post AND c.member = :member")
    List<String> findNickNamesByPostIdAndMemberId(Post post, Member member);

    @Query(value = "SELECT c.nickname FROM Comment c WHERE c.post.id = :postId")
    List<String> findNicknamesByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT c FROM Comment c WHERE c.post.id = :postId and c.parent.id is null")
    List<Comment> findCommentsByPostId(@Param("postId") Long postId);

    List<Comment> findRepliesByParent(Comment parent);

    void deleteAllByPost(Post post);

    @Query(value = "SELECT c FROM Comment c LEFT JOIN FETCH c.commentReports cr LEFT JOIN FETCH cr.reporter WHERE c.id = :commentId")
    Optional<Comment> findByCommentId(@Param("commentId") Long commentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE comment SET like_count = like_count + 1 WHERE comment_id = :commentId", nativeQuery = true)
    void increaseLikeCount(Long commentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE comment SET like_count = like_count - 1 WHERE comment_id = :commentId", nativeQuery = true)
    void decreaseLikeCount(Long commentId);
}
