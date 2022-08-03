package com.wooteco.sokdak.comment.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
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

        String nickname = getCommentNickname(newCommentRequest, authInfo, post);
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .nickname(nickname)
                .message(newCommentRequest.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    private String getCommentNickname(NewCommentRequest newCommentRequest, AuthInfo authInfo, Post post) {
        String memberNickname = memberRepository.findNicknameValueById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        if (!newCommentRequest.isAnonymous()) { // 기명이면 member table에서 memberId로 닉네임 가져옴.
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
        List<CommentResponse> commentResponses = commentRepository.findAllByPostId(postId)
                .stream()
                .map(it -> CommentResponse.of(it, authInfo.getId()))
                .collect(Collectors.toList());
        return new CommentsResponse(commentResponses);
    }

    public void deleteComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        comment.validateOwner(authInfo.getId());

        commentRepository.delete(comment);
    }
}
