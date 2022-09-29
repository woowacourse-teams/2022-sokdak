package com.wooteco.sokdak.like.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.like.domain.CommentLike;
import com.wooteco.sokdak.like.domain.Like;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.like.repository.CommentLikeRepository;
import com.wooteco.sokdak.like.repository.LikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LikeService {

    private static final int SPECIAL_BOARD_THRESHOLD = 5;

    private final BoardService boardService;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;

    public LikeService(BoardService boardService, LikeRepository likeRepository,
                       PostRepository postRepository,
                       CommentRepository commentRepository,
                       CommentLikeRepository commentLikeRepository,
                       MemberRepository memberRepository) {
        this.boardService = boardService;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public LikeFlipResponse flipPostLike(Long postId, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        flipPost(member, post);
        int likeCount = likeRepository.countByPost(post);
        boolean liked = likeRepository.existsByMemberIdAndPostId(member.getId(), post.getId());

        checkSpecialAndSave(likeCount, post);
        return new LikeFlipResponse(likeCount, liked);
    }

    private void flipPost(Member member, Post post) {
        Optional<Like> foundLike = likeRepository.findByMemberAndPost(member, post);
        if (foundLike.isPresent()) {
            likeRepository.delete(foundLike.get());
            return;
        }
        Like like = Like.builder()
                .member(member)
                .post(post)
                .build();
        likeRepository.save(like);
    }

    private void checkSpecialAndSave(int likeCount, Post post) {
        if (likeCount >= SPECIAL_BOARD_THRESHOLD) {
            boardService.checkAndSaveInSpecialBoard(post);
        }
    }

    @Transactional
    public LikeFlipResponse flipCommentLike(Long commentId, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        flipComment(member, comment);
        int likeCount = commentLikeRepository.countByCommentId(comment.getId());
        boolean liked = commentLikeRepository.existsByMemberIdAndCommentId(member.getId(), comment.getId());

        return new LikeFlipResponse(likeCount, liked);
    }

    private void flipComment(Member member, Comment comment) {
        Optional<CommentLike> foundCommentLike = commentLikeRepository.findByMemberIdAndCommentId(member.getId(),
                comment.getId());
        if (foundCommentLike.isPresent()) {
            commentLikeRepository.delete(foundCommentLike.get());
            return;
        }
        CommentLike commentLike = CommentLike.builder()
                .member(member)
                .comment(comment)
                .build();
        commentLikeRepository.save(commentLike);
    }
}
