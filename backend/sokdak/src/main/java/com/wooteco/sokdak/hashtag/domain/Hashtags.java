package com.wooteco.sokdak.hashtag.domain;

import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;

public class Hashtags {
    private final List<Hashtag> value;

    public Hashtags(List<Hashtag> value) {
        this.value = value;
    }

    public static Hashtags of(List<PostHashtag> postHashtags) {
        List<Hashtag> hashtags = postHashtags.stream()
                .map(PostHashtag::getHashtag)
                .collect(Collectors.toList());
        return new Hashtags(hashtags);
    }

    public List<String> getNames() {
        return value.stream()
                .map(Hashtag::getName)
                .collect(Collectors.toList());
    }

    public List<Hashtag> getValue() {
        return value;
    }

    public List<PostHashtag> getPostHashtags(Post post) {
        return value.stream()
                .map(hashtag -> PostHashtag.builder().post(post).hashtag(hashtag).build())
                .collect(Collectors.toList());
    }
}
