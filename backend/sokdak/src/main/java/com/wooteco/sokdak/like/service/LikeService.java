package com.wooteco.sokdak.like.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.like.domain.CommentLike;
import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.like.repository.CommentLikeRepository;
import com.wooteco.sokdak.like.repository.PostLikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LikeService {

    private static final int SPECIAL_BOARD_THRESHOLD = 5;

    private final BoardService boardService;
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    public LikeService(BoardService boardService, PostLikeRepository postLikeRepository,
                       PostRepository postRepository, CommentRepository commentRepository,
                       CommentLikeRepository commentLikeRepository, MemberRepository memberRepository,
                       AuthService authService) {
        this.boardService = boardService;
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.memberRepository = memberRepository;
        this.authService = authService;
    }

    @Transactional
    public LikeFlipResponse flipPostLike(Long postId, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        authService.checkAuthority(authInfo, post.getBoardId());

        int likeCount = flipPostLike(authInfo.getId(), post);
        boolean liked = post.hasLikeOfMember(authInfo.getId());

        checkSpecialAndSave(likeCount, post);
        return new LikeFlipResponse(likeCount, liked);
    }

    private int flipPostLike(Long memberId, Post post) {
        if (post.hasLikeOfMember(memberId)) {
            post.deleteLikeOfMember(memberId);
            postRepository.decreaseLikeCount(post.getId());
            return post.getLikeCount() - 1;
        }
        addNewPostLike(memberId, post);
        postRepository.increaseLikeCount(post.getId());
        return post.getLikeCount() + 1;
    }

    private void addNewPostLike(Long memberId, Post post) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        PostLike postLike = PostLike.builder()
                .member(member)
                .post(post)
                .build();
        postLikeRepository.save(postLike);
    }

    private void checkSpecialAndSave(int likeCount, Post post) {
        if (likeCount >= SPECIAL_BOARD_THRESHOLD) {
            boardService.checkAndSaveInSpecialBoard(post);
        }
    }

    @Transactional
    public LikeFlipResponse flipCommentLike(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        authService.checkAuthority(authInfo, comment.getBoardId());

        int likeCount = flipCommentLike(authInfo.getId(), comment);
        boolean liked = comment.hasLikeOfMember(authInfo.getId());

        return new LikeFlipResponse(likeCount, liked);
    }

    private int flipCommentLike(Long memberId, Comment comment) {
        if (comment.hasLikeOfMember(memberId)) {
            comment.deleteLikeOfMember(memberId);
            commentRepository.decreaseLikeCount(comment.getId());
            return comment.getCommentLikesCount() - 1;
        }
        addNewCommentLike(memberId, comment);
        commentRepository.increaseLikeCount(comment.getId());
        return comment.getCommentLikesCount() + 1;
    }

    private void addNewCommentLike(Long memberId, Comment comment) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        CommentLike commentLike = CommentLike.builder()
                .member(member)
                .comment(comment)
                .build();
        commentLikeRepository.save(commentLike);
    }
}
