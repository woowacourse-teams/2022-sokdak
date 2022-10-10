package com.wooteco.sokdak.like.repository;

import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    void deleteAllByPost(Post post);
}
