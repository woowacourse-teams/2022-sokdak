package com.wooteco.sokdak.post.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.hashtag.domain.Hashtags;
import com.wooteco.sokdak.hashtag.service.HashtagService;
import com.wooteco.sokdak.like.repository.LikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.MyPostsResponse;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
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
    private final PostBoardRepository postBoardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;

    public PostService(HashtagService hashtagService, BoardService boardService,
                       PostRepository postRepository, PostBoardRepository postBoardRepository,
                       MemberRepository memberRepository, CommentRepository commentRepository,
                       LikeRepository likeRepository, NotificationService notificationService) {
        this.hashtagService = hashtagService;
        this.boardService = boardService;
        this.postRepository = postRepository;
        this.postBoardRepository = postBoardRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Long addPost(Long boardId, NewPostRequest newPostRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        String writerNickname = createPostWriterNickname(newPostRequest.isAnonymous(), member);
        Post post = Post.builder()
                .title(newPostRequest.getTitle())
                .content(newPostRequest.getContent())
                .writerNickname(writerNickname)
                .member(member)
                .imageName(newPostRequest.getImageName())
                .build();
        Post savedPost = postRepository.save(post);

        hashtagService.saveHashtag(newPostRequest.getHashtags(), savedPost);
        boardService.savePostBoard(savedPost, boardId, authInfo.getRole());
        return savedPost.getId();
    }

    private String createPostWriterNickname(boolean anonymous, Member member) {
        if (anonymous) {
            return RandomNicknameGenerator.generate(Collections.emptySet());
        }
        return member.getNickname();
    }

    public PostDetailResponse findPost(Long postId, AuthInfo authInfo) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        List<PostBoard> postBoards = postBoardRepository.findPostBoardsByPostId(foundPost.getId());
        boolean liked = likeRepository.existsByMemberIdAndPostId(authInfo.getId(), postId);
        Hashtags hashtags = hashtagService.findHashtagsByPostId(postId);

        return PostDetailResponse.of(foundPost, postBoards.get(0), liked,
                foundPost.isAuthorized(authInfo.getId()), hashtags, foundPost.getImageName());
    }

    public PostsResponse findPostsByBoard(Long boardId, Pageable pageable) {
        Slice<PostBoard> postBoards = postBoardRepository.findPostBoardsByBoardId(boardId, pageable);
        return PostsResponse.ofPostBoardSlice(postBoards);
    }

    public MyPostsResponse findMyPosts(Pageable pageable, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Page<Post> posts = postRepository.findPostsByMemberOrderByCreatedAtDesc(pageable, member);
        return MyPostsResponse.of(posts.getContent(), posts.getTotalPages());
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Hashtags hashtags = hashtagService.findHashtagsByPostId(post.getId());

        // Todo: validateOwner 메서드 하나만 실행하게 리팩터링하기
        post.updateTitle(postUpdateRequest.getTitle(), authInfo.getId());
        post.updateContent(postUpdateRequest.getContent(), authInfo.getId());
        post.updateImageName(postUpdateRequest.getImageName(), authInfo.getId());

        hashtagService.deleteAllByPostId(hashtags, post.getId());
        hashtagService.saveHashtag(postUpdateRequest.getHashtags(), post);
    }

    @Transactional
    public void deletePost(Long id, AuthInfo authInfo) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        post.validateOwner(authInfo.getId());
        Hashtags hashtags = hashtagService.findHashtagsByPostId(post.getId());

        commentRepository.deleteAllByPost(post);
        likeRepository.deleteAllByPostId(post.getId());
        hashtagService.deleteAllByPostId(hashtags, id);
        notificationService.deletePostNotification(id);

        postRepository.delete(post);
    }
}
