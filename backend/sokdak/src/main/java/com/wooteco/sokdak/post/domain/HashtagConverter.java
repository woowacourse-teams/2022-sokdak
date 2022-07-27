package com.wooteco.sokdak.post.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HashtagConverter {

    private HashtagConverter(){
    }

    public List<Hashtag> PostHashtagsToHashtags(List<PostHashtag> postHashtags) {
        return postHashtags.stream()
                .map(PostHashtag::getHashtag)
                .collect(Collectors.toList());
    }

    public List<Hashtag> NamesToHashtags(List<String> names) {
        return names.stream()
                .map(name -> Hashtag.builder().name(name).build())
                .collect(Collectors.toList());
    }

    public List<PostHashtag> HashtagsToPostHashtags(List<Hashtag> hashtags, Post post) {
        return hashtags.stream()
                .map(hashtag -> PostHashtag.builder().hashtag(hashtag).post(post).build())
                .collect(Collectors.toList());
    }
}
