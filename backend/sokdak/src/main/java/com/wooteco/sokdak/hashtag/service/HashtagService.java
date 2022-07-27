package com.wooteco.sokdak.hashtag.service;

import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.hashtag.repository.PostHashtagRepository;
import com.wooteco.sokdak.hashtag.util.HashtagConverter;
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
    private final HashtagConverter hashtagConverter;

    public HashtagService(HashtagRepository hashtagRepository,
                          PostHashtagRepository postHashtagRepository,
                          HashtagConverter hashtagConverter) {
        this.hashtagRepository = hashtagRepository;
        this.postHashtagRepository = postHashtagRepository;
        this.hashtagConverter = hashtagConverter;
    }

    @Transactional
    public void saveHashtags(List<String> names, Post savedPost) {
        List<Hashtag> hashtags = hashtagConverter.NamesToHashtags(names);

        List<Hashtag> saveHashtags = hashtags.stream()
                .filter(it -> !hashtagRepository.existsByName(it.getName()))
                .collect(Collectors.toList());
        hashtagRepository.saveAll(saveHashtags);

        List<Hashtag> realHashtags = names.stream()
                .map(name -> hashtagRepository.findByName(name).orElseThrow())
                .collect(Collectors.toList());
        postHashtagRepository.saveAll(hashtagConverter
                .HashtagsToPostHashtags(realHashtags, savedPost));
    }

    public List<Hashtag> findHashtagsByPostId(Long postId) {
        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPostId(postId);
        return hashtagConverter.PostHashtagsToHashtags(postHashtags);
    }

    @Transactional
    public void deleteAllByPostId(List<Hashtag> hashtags, Long id) {
        postHashtagRepository.deleteAllByPostId(id);
        deleteNoUsedHashtags(hashtags);
    }

    private void deleteNoUsedHashtags(List<Hashtag> hashtags) {
        for (Hashtag hashtag : hashtags) {
            if (!postHashtagRepository.existsByHashtagId(hashtag.getId())) {
                hashtagRepository.delete(hashtag);
            }
        }
    }
}
