package com.wooteco.sokdak.post.service;

import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Long addPost(NewPostRequest newPostRequest) {
        Post post = newPostRequest.toEntity();
        return postRepository.save(post).getId();
    }

    public PostResponse findPost(Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.from(foundPost);
    }

    public PostsResponse findPosts(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Slice<Post> posts = postRepository.findSliceBy(pageRequest);
        List<PostResponse> postResponses = posts.getContent()
                .stream()
                .map(PostResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new PostsResponse(postResponses, posts.isLast());
    }
}
