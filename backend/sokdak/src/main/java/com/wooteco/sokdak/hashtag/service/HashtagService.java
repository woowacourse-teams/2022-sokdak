package com.wooteco.sokdak.hashtag.service;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.hashtag.repository.PostHashtagRepository;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
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
}
