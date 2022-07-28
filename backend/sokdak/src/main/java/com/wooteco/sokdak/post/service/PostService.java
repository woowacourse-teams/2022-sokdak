package com.wooteco.sokdak.post.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.service.HashtagService;
import com.wooteco.sokdak.like.repository.LikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
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

    private final HashtagService hashtagService;
    private final BoardService boardService;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public PostService(HashtagService hashtagService, BoardService boardService,
                       PostRepository postRepository, MemberRepository memberRepository,
                       CommentRepository commentRepository, LikeRepository likeRepository) {
        this.hashtagService = hashtagService;
        this.boardService = boardService;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public Long addPost(Long boardId, NewPostRequest newPostRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        Post post = Post.builder()
                .title(newPostRequest.getTitle())
                .content(newPostRequest.getContent())
                .member(member)
                .build();
        Post savedPost = postRepository.save(post);

        hashtagService.saveHashtag(newPostRequest.getHashtags(), savedPost);
        boardService.savePostBoard(savedPost, boardId);
        return savedPost.getId();
    }

    public PostDetailResponse findPost(Long postId, AuthInfo authInfo) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        boolean liked = likeRepository.existsByMemberIdAndPostId(authInfo.getId(), postId);
        Hashtags hashtags = hashtagService.findHashtagsByPostId(postId);

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
        Hashtags hashtags = hashtagService.findHashtagsByPostId(post.getId());

        post.updateTitle(postUpdateRequest.getTitle(), authInfo.getId());
        post.updateContent(postUpdateRequest.getContent(), authInfo.getId());

        hashtagService.deleteAllByPostId(hashtags, post.getId());
        hashtagService.saveHashtag(postUpdateRequest.getHashtags(), post);
    }

    @Transactional
    public void deletePost(Long id, AuthInfo authInfo) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        post.validateOwner(authInfo.getId());
        Hashtags hashtags = hashtagService.findHashtagsByPostId(post.getId());

        commentRepository.deleteAllByPostId(post.getId());
        likeRepository.deleteAllByPostId(post.getId());
        hashtagService.deleteAllByPostId(hashtags, id);

        postRepository.delete(post);
    }
}
