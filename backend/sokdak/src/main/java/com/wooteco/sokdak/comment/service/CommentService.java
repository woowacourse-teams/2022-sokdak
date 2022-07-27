package com.wooteco.sokdak.comment.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
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

        String nickname = getCommentNickname(newCommentRequest, authInfo, postId);
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .nickname(nickname)
                .message(newCommentRequest.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    private String getCommentNickname(NewCommentRequest newCommentRequest, AuthInfo authInfo, Long postId) {
        String memberNickname = memberRepository.findNicknameValueById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        if (!newCommentRequest.isAnonymous()) { // 기명이면 member table에서 memberId로 닉네임 가져옴.
            return memberNickname;
        }
        // 익명이면, comment table에서 memberId에 해당하는 nickname들을 다 가져와서, member
        List<String> nickNamesByMember = commentRepository.findNickNamesByPostIdAndMemberId(postId, authInfo.getId());
        List<String> usedNicknames = commentRepository.findNicknamesByPostId(postId);

        return nickNamesByMember.stream()
                .filter(nickname -> !nickname.equals(memberNickname))
                .findAny()
                .orElseGet(() -> RandomNicknameGenerator.generate(new HashSet<>(usedNicknames)));
    }

    public CommentsResponse findComments(Long postId) {
        List<CommentResponse> commentResponses = commentRepository.findAllByPostId(postId)
                .stream()
                .map(CommentResponse::of)
                .collect(Collectors.toList());
        return new CommentsResponse(commentResponses);
    }
}
