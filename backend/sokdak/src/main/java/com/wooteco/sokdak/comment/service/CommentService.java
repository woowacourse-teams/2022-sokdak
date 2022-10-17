package com.wooteco.sokdak.comment.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.NewReplyRequest;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.exception.ReplyDepthException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.like.repository.CommentLikeRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    private final CommentLikeRepository commentLikeRepository;
    private final AuthService authService;

    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository,
                          PostRepository postRepository, NotificationService notificationService,
                          CommentLikeRepository commentLikeRepository,
                          AuthService authService) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
        this.commentLikeRepository = commentLikeRepository;
        this.authService = authService;
    }

    @Transactional
    public Long addComment(Long postId, NewCommentRequest newCommentRequest, AuthInfo authInfo) {
        authService.checkAuthority(authInfo, newCommentRequest.getBoardId());
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        String nickname = getCommentNickname(newCommentRequest.isAnonymous(), authInfo, post);

        Comment comment = Comment.parent(member, post, nickname, newCommentRequest.getContent());

        commentRepository.save(comment);
        notificationService.notifyCommentIfNotMine(post.getMember(), post, comment);

        return comment.getId();
    }

    @Transactional
    public Long addReply(Long commentId, NewReplyRequest newReplyRequest, AuthInfo authInfo) {
        authService.checkAuthority(authInfo, newReplyRequest.getBoardId());
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!parent.isParent()){
            throw new ReplyDepthException();
        }
        Post post = parent.getPost();

        String nickname = getCommentNickname(newReplyRequest.isAnonymous(), authInfo, post);

        Comment reply = Comment.child(member, post, nickname, newReplyRequest.getContent(), parent);

        commentRepository.save(reply);
        notificationService.notifyReplyIfNotMine(parent.getMember(), post, parent, reply);
        return reply.getId();
    }

    private String getCommentNickname(boolean anonymous, AuthInfo authInfo, Post post) {
        String memberNickname = memberRepository.findNicknameValueById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        if (!anonymous) { // 기명이면 member table에서 memberId로 닉네임 가져옴.
            return memberNickname;
        }
        if (post.isOwner(authInfo.getId())) { // 댓글 작성하는 사람과 게시글 작성자 일치
            return getPostWriterNickname(post, authInfo.getId());
        }

        // 익명이면, comment table에서 memberId에 해당하는 nickname들을 다 가져와서, member
        List<String> usedNicknames = getUsedNicknameInPage(post);
        List<String> pastNicknamesByMemberId =
                commentRepository.findNickNamesByPostIdAndMemberId(post.getId(), authInfo.getId());

        return pastNicknamesByMemberId.stream()
                .filter(isRandomNicknameGeneratedInPast(memberNickname))
                .findAny()
                .orElseGet(() -> RandomNicknameGenerator.generate(new HashSet<>(usedNicknames)));
    }

    private String getPostWriterNickname(Post post, Long memberId) {
        if (post.isAnonymous()) { // 작성자가 기명으로 글 작성
            return post.getNickname();
        }
        if (commentRepository.existsByPostAndMemberId(post, memberId)) {
            return commentRepository.findNickNamesByPostIdAndMemberId(post.getId(), memberId)
                    .get(0);
        }
        List<String> commentNicknames = commentRepository.findNicknamesByPostId(post.getId());
        return RandomNicknameGenerator.generate(new HashSet<>(commentNicknames));
    }

    private Predicate<String> isRandomNicknameGeneratedInPast(String memberNickname) {
        return nickname -> !nickname.equals(memberNickname);
    }

    private List<String> getUsedNicknameInPage(Post post) {
        List<String> usedNicknames = commentRepository.findNicknamesByPostId(post.getId());
        usedNicknames.add(post.getNickname());
        return usedNicknames;
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
        boolean liked = commentLikeRepository.existsByMemberIdAndCommentId(id, comment.getId());
        return CommentResponse.of(comment, id, convertToReplyResponses(comment, id), liked);
    }

    private List<ReplyResponse> convertToReplyResponses(Comment parent, Long accessMemberId) {
        final List<Comment> replies = commentRepository.findRepliesByParent(parent);
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Comment reply : replies) {
            boolean liked = commentLikeRepository.existsByMemberIdAndCommentId(accessMemberId, reply.getId());
            replyResponses.add(ReplyResponse.of(reply, accessMemberId, liked));
        }
        return replyResponses;
    }

    @Transactional
    public void deleteComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        comment.validateOwner(authInfo.getId());
        notificationService.deleteCommentNotification(commentId);
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
