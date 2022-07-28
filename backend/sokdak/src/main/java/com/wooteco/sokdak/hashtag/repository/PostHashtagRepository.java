package com.wooteco.sokdak.hashtag.repository;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.hashtag.dto.HashtagSearchElementResponse;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import javax.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

    List<PostHashtag> findAllByPostId(Long id);

    void deleteAllByPostId(Long postId);

    boolean existsByHashtagId(Long id);

    @Query(value = "SELECT p FROM Post p INNER JOIN PostHashtag ph ON p=ph.post and ph.hashtag.id = :hashtagId")
    Slice<Post> findAllPostByHashtagId(Long hashtagId, Pageable pageable);

    int countByHashtagId(Long hashtagId);

    @Query(value = "SELECT ph.hashtag, COUNT(ph.hashtag.id) FROM PostHashtag ph WHERE ph.hashtag IN :hashtags GROUP BY ph.hashtag ORDER BY COUNT(ph.hashtag.id) DESC")
    List<Tuple> findAllByHashtagOrderByCount(List<Hashtag> hashtags, Pageable pageable);
}
