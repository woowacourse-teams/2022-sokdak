package com.wooteco.sokdak.post.repository;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsByMemberOrderByCreatedAtDesc(Pageable pageable, Member member);

    @Query(value = "SELECT p FROM Post p LEFT JOIN FETCH p.postLikes WHERE p.id = :id")
    Optional<Post> findById(Long id);
}
