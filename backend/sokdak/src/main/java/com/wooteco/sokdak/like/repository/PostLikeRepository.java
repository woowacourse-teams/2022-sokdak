package com.wooteco.sokdak.like.repository;

import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    int countByPost(Post post);

    @Query(value = "SELECT exists (SELECT * from post_likes WHERE member_id = :memberId and post_id = :postId)",
            nativeQuery = true)
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteAllByPost(Post post);
}
