package com.wooteco.sokdak.post.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long addPost(NewPostRequest newPostRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = Post.builder()
                .title(newPostRequest.getTitle())
                .content(newPostRequest.getContent())
                .member(member)
                .build();
        return postRepository.save(post).getId();
    }

    public PostResponse findPost(Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.from(foundPost);
    }


    public PostsResponse findPosts(Pageable pageable) {
        Slice<Post> posts = postRepository.findSliceBy(pageable);
        List<PostResponse> postResponses = posts.getContent()
                .stream()
                .map(PostResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new PostsResponse(postResponses, posts.isLast());
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        post.updateTitle(postUpdateRequest.getTitle(), authInfo.getId());
        post.updateContent(postUpdateRequest.getContent(), authInfo.getId());
    }

    @Transactional
    public void deletePost(Long id, AuthInfo authInfo) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        post.validateOwner(authInfo.getId());
        postRepository.delete(post);
    }
}
