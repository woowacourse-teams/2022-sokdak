package com.wooteco.sokdak.like.repository;

import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    void deleteAllByPost(Post post);

    boolean existsByPostAndMemberId(Post post, Long memberId);

    Optional<PostLike> findByPostAndMemberId(Post post, Long memberId);

    Optional<PostLike> findByPostAndMember(Post post, Member member);
}
