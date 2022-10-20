package com.wooteco.sokdak.post.repository;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {

    String SEARCH_SQL = "SELECT * from post where "
            + "(:query is null or :query = '') "
            + "or "
            + "(title regexp :query) "
            + "or "
            + "(content regexp :query) ";

    Page<Post> findPostsByMemberOrderByCreatedAtDesc(Pageable pageable, Member member);

    @Query(value = SEARCH_SQL, nativeQuery = true)
    Page<Post> findPostPagesByQuery(Pageable pageable, String query);

    @Query(value = SEARCH_SQL, nativeQuery = true)
    Slice<Post> findPostSlicePagesByQuery(Pageable pageable, String query);

    @Query(value = "SELECT p FROM Post p LEFT JOIN FETCH p.postLikes WHERE p.id = :id")
    Optional<Post> findById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE post SET view_count = view_count + 1 WHERE post_id = :postId", nativeQuery = true)
    void updateViewCount(Long postId);
}
