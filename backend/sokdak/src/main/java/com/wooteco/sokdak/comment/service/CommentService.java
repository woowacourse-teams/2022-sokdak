package com.wooteco.sokdak.comment.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.domain.CommentDeletionEvent;
import com.wooteco.sokdak.comment.domain.CommentNicknameGenerator;
import com.wooteco.sokdak.comment.domain.NewCommentEvent;
import com.wooteco.sokdak.comment.domain.NewReplyEvent;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.NewReplyRequest;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
import com.wooteco.sokdak.comment.event.CommentDeletionEvent;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.exception.ReplyDepthException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.like.repository.CommentLikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final AuthService authService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CommentNicknameGenerator commentNicknameGenerator;

    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository,
                          PostRepository postRepository, NotificationService notificationService,
                          CommentLikeRepository commentLikeRepository, AuthService authService,
                          ApplicationEventPublisher applicationEventPublisher,
                          CommentNicknameGenerator commentNicknameGenerator) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.authService = authService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.commentNicknameGenerator = commentNicknameGenerator;
    }

    @Transactional
    public Long addComment(Long postId, NewCommentRequest newCommentRequest, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        authService.checkAuthority(authInfo, post.getBoardId());
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        String nickname = commentNicknameGenerator.getCommentNickname(newCommentRequest.isAnonymous(), authInfo, post);

        Comment comment = Comment.parent(member, post, nickname, newCommentRequest.getContent());

        commentRepository.save(comment);
        applicationEventPublisher.publishEvent(
                new NewCommentEvent(post.getMember().getId(), post.getId(), member.getId()));

        return comment.getId();
    }

    @Transactional
    public Long addReply(Long commentId, NewReplyRequest newReplyRequest, AuthInfo authInfo) {
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        authService.checkAuthority(authInfo, parent.getBoardId());
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        if (!parent.isParent()) {
            throw new ReplyDepthException();
        }
        Post post = parent.getPost();

        String nickname = commentNicknameGenerator.getCommentNickname(newReplyRequest.isAnonymous(), authInfo, post);

        Comment reply = Comment.child(member, post, nickname, newReplyRequest.getContent(), parent);

        commentRepository.save(reply);
        applicationEventPublisher.publishEvent(new NewReplyEvent(
                parent.getMember().getId(), post.getId(), parent.getId(), member.getId()));
        return reply.getId();
    }

    public CommentsResponse findComments(Long postId, AuthInfo authInfo) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(it -> convertToCommentResponse(authInfo, it))
                .collect(Collectors.toList());
        int numOfComment = commentResponses.size();
        int numOfReply = commentResponses.stream()
                .map(it -> it.getReplies().size())
                .reduce(Integer::sum).orElse(0);
        return new CommentsResponse(commentResponses, numOfComment + numOfReply);
    }

    private CommentResponse convertToCommentResponse(AuthInfo authInfo, Comment comment) {
        Long id = authInfo.getId();
        if (comment.isSoftRemoved()) {
            return CommentResponse.softRemovedOf(comment, convertToReplyResponses(comment, id));
        }
        boolean liked = commentLikeRepository.existsByMemberIdAndComment(id, comment);
        return CommentResponse.of(comment, id, convertToReplyResponses(comment, id), liked);
    }

    private List<ReplyResponse> convertToReplyResponses(Comment parent, Long accessMemberId) {
        final List<Comment> replies = commentRepository.findRepliesByParent(parent);
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Comment reply : replies) {
            boolean liked = commentLikeRepository.existsByMemberIdAndComment(accessMemberId, reply);
            replyResponses.add(ReplyResponse.of(reply, accessMemberId, liked));
        }
        return replyResponses;
    }

    @Transactional
    public void deleteComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        comment.validateOwner(authInfo.getId());
        applicationEventPublisher.publishEvent(new CommentDeletionEvent(comment.getId()));
        commentLikeRepository.deleteAllByCommentId(commentId);

        deleteCommentOrReply(comment);
    }

    private void deleteCommentOrReply(Comment comment) {
        if (comment.isParent()) {
            deleteParent(comment);
            return;
        }

        deleteChild(comment);
    }

    private void deleteParent(Comment comment) {
        if (comment.hasNoReply()) {
            commentRepository.delete(comment);
            return;
        }
        comment.changePretendingToBeRemoved();
    }

    private void deleteChild(Comment comment) {
        Comment parent = comment.getParent();
        parent.deleteChild(comment);
        commentRepository.delete(comment);

        if (parent.hasNoReply() && parent.isSoftRemoved()) {
            commentRepository.delete(parent);
        }
    }
}
