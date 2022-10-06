package com.wooteco.sokdak.like.repository;

import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    int countByPost(Post post);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteAllByPost(Post post);
}
