package com.wooteco.sokdak.comment.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.NewReplyRequest;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.exception.ReplyDepthException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository,
                          MemberRepository memberRepository,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Long addComment(Long postId, NewCommentRequest newCommentRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        String nickname = getCommentNickname(newCommentRequest.isAnonymous(), authInfo, post);

        Comment comment = Comment.parent(member, post, nickname, newCommentRequest.getContent());

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public Long addReply(Long commentId, NewReplyRequest newReplyRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!Objects.isNull(parent.getParent())) {
            throw new ReplyDepthException();
        }
        Post post = parent.getPost();

        String nickname = getCommentNickname(newReplyRequest.isAnonymous(), authInfo, post);

        Comment reply = Comment.child(member, post, nickname, newReplyRequest.getContent(), parent);

        commentRepository.save(reply);
        return reply.getId();
    }

    private String getCommentNickname(boolean anonymous, AuthInfo authInfo, Post post) {
        String memberNickname = memberRepository.findNicknameValueById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        if (!anonymous) { // 기명이면 member table에서 memberId로 닉네임 가져옴.
            return memberNickname;
        }
        if (post.isAuthenticated(authInfo.getId())) { // 댓글 작성하는 사람과 게시글 작성자 일치
            return getPostWriterNickname(post);
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

    private String getPostWriterNickname(Post post) {
        if (post.isAnonymous()) { // 작성자가 기명으로 글 작성
            return post.getNickname();
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
        List<Comment> comments = commentRepository.findAllByPostIdAndParentId(postId, null);
        List<CommentResponse> commentResponses = comments.stream()
                .map(it -> convertToCommentResponse(postId, authInfo, it))
                .collect(Collectors.toList());

        return new CommentsResponse(commentResponses);
    }

    private CommentResponse convertToCommentResponse(Long postId, AuthInfo authInfo, Comment comment) {
        if (comment.isSoftRemoved()) {
            return CommentResponse.softRemovedOf(comment, findReplies(comment, postId, authInfo.getId()));
        }
        return CommentResponse.of(comment, authInfo.getId(), findReplies(comment, postId, authInfo.getId()));
    }

    private Map<Comment, Long> findReplies(Comment parent, Long postId, Long accessMemberId) {
        List<Comment> replies = commentRepository.findAllByPostIdAndParentId(postId, parent.getId());
        Map<Comment, Long> accessMemberIdByReply = new LinkedHashMap<>();
        for (Comment reply : replies) {
            accessMemberIdByReply.put(reply, accessMemberId);
        }
        return accessMemberIdByReply;
    }

    @Transactional
    public void deleteComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        comment.validateOwner(authInfo.getId());

        Comment parent = comment.getParent();
        if (Objects.isNull(parent)) {       // 댓글 삭제
            if (comment.getChildren().isEmpty()) {
                commentRepository.delete(comment);
                return;
            }
            comment.changePretendingToBeRemoved();
            return;
        }
        // 대댓글 삭제
        parent.deleteChild(comment);
        commentRepository.delete(comment);

        if (parent.getChildren().isEmpty() && parent.isSoftRemoved()) {
            commentRepository.delete(parent);
        }
    }
}
