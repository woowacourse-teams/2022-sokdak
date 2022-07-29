package com.wooteco.sokdak.hashtag.service;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.dto.HashtagSearchElementResponse;
import com.wooteco.sokdak.hashtag.dto.HashtagsSearchRequest;
import com.wooteco.sokdak.hashtag.dto.HashtagsSearchResponse;
import com.wooteco.sokdak.hashtag.exception.HashtagNotFoundException;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.hashtag.repository.PostHashtagRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.PostsResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

    public HashtagService(HashtagRepository hashtagRepository,
                          PostHashtagRepository postHashtagRepository) {
        this.hashtagRepository = hashtagRepository;
        this.postHashtagRepository = postHashtagRepository;
    }

    @Transactional
    public void saveHashtag(List<String> names, Post savedPost) {
        Hashtags hashtags = new Hashtags(names.stream()
                .map(this::saveOrFind)
                .collect(Collectors.toList()));
        postHashtagRepository.saveAll(hashtags.getPostHashtags(savedPost));
    }

    private Hashtag saveOrFind(String name) {
        return hashtagRepository
                .findByName(name)
                .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(name).build()));
    }

    public Hashtags findHashtagsByPostId(Long postId) {
        return Hashtags.ofPostHashtags(postHashtagRepository.findAllByPostId(postId));
    }

    @Transactional
    public void deleteAllByPostId(Hashtags hashtags, Long id) {
        postHashtagRepository.deleteAllByPostId(id);
        deleteNoUsedHashtags(hashtags);
    }

    private void deleteNoUsedHashtags(Hashtags hashtags) {
        for (Hashtag hashtag : hashtags.getValue()) {
            if (!postHashtagRepository.existsByHashtagId(hashtag.getId())) {
                hashtagRepository.delete(hashtag);
            }
        }
    }

    public PostsResponse findPostsWithHashtag(String name, Pageable pageable) {
        Hashtag hashtag = hashtagRepository.findByName(name)
                .orElseThrow(HashtagNotFoundException::new);
        Slice<Post> posts = postHashtagRepository.findAllPostByHashtagId(hashtag.getId(), pageable);
        return PostsResponse.ofSlice(posts);
    }

    public HashtagsSearchResponse findHashtagsWithTagName(HashtagsSearchRequest hashtagsSearchRequest) {
        List<Hashtag> contains = hashtagRepository.findAllByNameContains(hashtagsSearchRequest.getInclude());
        if (contains.isEmpty()) {
            return new HashtagsSearchResponse(new ArrayList<>());
        }

        List<Tuple> hashtagOrderByCount = postHashtagRepository.findAllByHashtagOrderByCount(
                contains, PageRequest.of(0, hashtagsSearchRequest.getLimit()));

        List<HashtagSearchElementResponse> hashtagSearchElementResponses = hashtagOrderByCount.stream()
                .map(tuple -> new HashtagSearchElementResponse(tuple.get(0, Hashtag.class), tuple.get(1, Long.class)))
                .collect(Collectors.toList());
        return new HashtagsSearchResponse(hashtagSearchElementResponses);
    }
}
