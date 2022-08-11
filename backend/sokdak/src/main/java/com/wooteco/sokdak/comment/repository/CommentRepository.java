package com.wooteco.sokdak.comment.repository;

import com.wooteco.sokdak.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c.nickname FROM Comment c WHERE c.member.id = :memberId and c.post.id = :postId")
    List<String> findNickNamesByPostIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);

    @Query(value = "SELECT c.nickname FROM Comment c WHERE c.post.id = :postId")
    List<String> findNicknamesByPostId(@Param("postId") Long postId);

    List<Comment> findAllByPostId(Long postId);

    List<Comment> findAllByPostIdAndParentId(Long postId, Long parentId);

    void deleteAllByPostId(Long id);
}
