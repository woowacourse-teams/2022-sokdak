package com.wooteco.sokdak.post.service;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.service.HashtagService;
import com.wooteco.sokdak.like.repository.PostLikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.domain.SearchQuery;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PagePostsResponse;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsCountResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.Collections;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final HashtagService hashtagService;
    private final BoardService boardService;
    private final PostRepository postRepository;
    private final PostBoardRepository postBoardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationService notificationService;
    private final AuthService authService;

    public PostService(HashtagService hashtagService, BoardService boardService,
                       PostRepository postRepository, PostBoardRepository postBoardRepository,
                       MemberRepository memberRepository, CommentRepository commentRepository,
                       PostLikeRepository postLikeRepository, NotificationService notificationService,
                       AuthService authService) {
        this.hashtagService = hashtagService;
        this.boardService = boardService;
        this.postRepository = postRepository;
        this.postBoardRepository = postBoardRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
        this.notificationService = notificationService;
        this.authService = authService;
    }

    @Transactional
    public Long addPost(Long boardId, NewPostRequest newPostRequest, AuthInfo authInfo) {
        authService.checkAuthority(authInfo, boardId);
        Member member = findMember(authInfo);
        String writerNickname = createPostWriterNickname(newPostRequest.isAnonymous(), member);
        Post post = createPost(newPostRequest, writerNickname, member);
        Post savedPost = postRepository.save(post);

        hashtagService.saveHashtag(newPostRequest.getHashtags(), savedPost);
        boardService.savePostBoard(savedPost, boardId, authInfo.getRole());
        return savedPost.getId();
    }

    private Post createPost(NewPostRequest newPostRequest, String writerNickname, Member member) {
        return Post.builder()
                .title(newPostRequest.getTitle())
                .content(newPostRequest.getContent())
                .writerNickname(writerNickname)
                .member(member)
                .imageName(newPostRequest.getImageName())
                .build();
    }

    private String createPostWriterNickname(boolean anonymous, Member member) {
        if (anonymous) {
            return RandomNicknameGenerator.generate(Collections.emptySet());
        }
        return member.getNickname();
    }

    public PostDetailResponse findPost(Long postId, AuthInfo authInfo) {
        Post foundPost = findPostObject(postId);
        Board writableBoard = foundPost.getWritableBoard();
        boolean liked = foundPost.hasLikeOfMember(authInfo.getId());
        Hashtags hashtags = hashtagService.findHashtagsByPost(foundPost);

        return PostDetailResponse.of(foundPost, writableBoard, liked,
                foundPost.isOwner(authInfo.getId()), hashtags, foundPost.getImageName());
    }

    public void updateViewCount(Long postId) {
        postRepository.updateViewCount(postId);
    }

    private Post findPostObject(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    public PostsResponse findPostsByBoard(Long boardId, Pageable pageable) {
        Slice<Post> posts = postBoardRepository.findPostsByBoardId(boardId, pageable);
        return PostsResponse.ofPostSlice(posts);
    }

    public PagePostsResponse findMyPosts(Pageable pageable, AuthInfo authInfo) {
        Member member = findMember(authInfo);
        Page<Post> posts = postRepository.findPostsByMemberOrderByCreatedAtDesc(pageable, member);
        return PagePostsResponse.of(posts.getContent(), posts.getTotalPages(), (int) posts.getTotalElements());
    }

    public PostsCountResponse countPostWithQuery(String query) {
        Pageable pageable = PageRequest.of(0, 3, DESC, "created_at");
        SearchQuery searchQuery = new SearchQuery(query);

        Page<Post> posts = postRepository.findPostPagesByQuery(pageable, searchQuery.getValue());
        return PostsCountResponse.of((int) posts.getTotalElements());
    }

    public PostsResponse searchSliceWithQuery(@Nullable String query,
                                              Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), DESC, "created_at");
        SearchQuery searchQuery = new SearchQuery(query);
        Slice<Post> posts = postRepository.findPostSlicePagesByQuery(pageable, searchQuery.getValue());
        return PostsResponse.ofPostSlice(posts);
    }

    private Member findMember(AuthInfo authInfo) {
        return memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, AuthInfo authInfo) {
        Post post = findPostObject(postId);
        Hashtags hashtags = hashtagService.findHashtagsByPost(post);

        validateOwner(authInfo, post);
        post.updateTitle(postUpdateRequest.getTitle());
        post.updateContent(postUpdateRequest.getContent());
        post.updateImageName(postUpdateRequest.getImageName());

        hashtagService.deleteAllByPost(hashtags, post);
        hashtagService.saveHashtag(postUpdateRequest.getHashtags(), post);
    }

    @Transactional
    public void deletePost(Long id, AuthInfo authInfo) {
        Post post = findPostObject(id);
        validateOwner(authInfo, post);

        Hashtags hashtags = hashtagService.findHashtagsByPost(post);

        commentRepository.deleteAllByPost(post);
        postLikeRepository.deleteAllByPost(post);
        postLikeRepository.deleteAllByPost(post);
        hashtagService.deleteAllByPost(hashtags, post);
        notificationService.deletePostNotification(id);

        postRepository.delete(post);
    }

    private void validateOwner(AuthInfo authInfo, Post post) {
        if (!post.isOwner(authInfo.getId())) {
            throw new AuthorizationException();
        }
    }
}
