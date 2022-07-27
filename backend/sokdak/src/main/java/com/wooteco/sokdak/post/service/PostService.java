package com.wooteco.sokdak.post.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.like.repository.LikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Hashtag;
import com.wooteco.sokdak.post.domain.HashtagConverter;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.domain.PostHashtag;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.HashtagRepository;
import com.wooteco.sokdak.post.repository.PostHashtagRepository;
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
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagConverter hashtagConverter;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;


    public PostService(PostRepository postRepository, MemberRepository memberRepository,
                       HashtagRepository hashtagRepository,
                       PostHashtagRepository postHashtagRepository,
                       CommentRepository commentRepository,
                       LikeRepository likeRepository, HashtagConverter hashtagConverter) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.hashtagRepository = hashtagRepository;
        this.postHashtagRepository = postHashtagRepository;
        this.hashtagConverter = hashtagConverter;
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
        Post savedPost = postRepository.save(post);

        saveHashtags(newPostRequest.getHashtags(), savedPost);

        return savedPost.getId();
    }

    private void saveHashtags(List<String> names, Post savedPost) {
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

    public PostDetailResponse findPost(Long postId, AuthInfo authInfo) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        boolean liked = likeRepository.existsByMemberIdAndPostId(authInfo.getId(), postId);

        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPostId(postId);
        List<Hashtag> hashtags = hashtagConverter.PostHashtagsToHashtags(postHashtags);
        return PostDetailResponse.of(foundPost, liked, foundPost.isAuthenticated(authInfo.getId()), hashtags);
    }

    public PostsResponse findPosts(Pageable pageable) {
        Slice<Post> posts = postRepository.findSliceBy(pageable);
        List<PostsElementResponse> postsElementResponses = posts.getContent()
                .stream()
                .map(PostsElementResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new PostsResponse(postsElementResponses, posts.isLast());
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        post.updateTitle(postUpdateRequest.getTitle(), authInfo.getId());
        post.updateContent(postUpdateRequest.getContent(), authInfo.getId());

        postHashtagRepository.deleteAllByPostId(post.getId());
        saveHashtags(postUpdateRequest.getHashtags(), post);
    }

    @Transactional
    public void deletePost(Long id, AuthInfo authInfo) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        post.validateOwner(authInfo.getId());

        List<Hashtag> hashtags = hashtagConverter.PostHashtagsToHashtags(postHashtagRepository.findAllByPostId(id));

        commentRepository.deleteAllByPostId(post.getId());
        likeRepository.deleteAllByPostId(post.getId());
        postHashtagRepository.deleteAllByPostId(id);

        postRepository.delete(post);

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
