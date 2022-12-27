package com.wooteco.sokdak.comment.domain;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.post.domain.Post;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CommentNicknameGenerator {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public CommentNicknameGenerator(MemberRepository memberRepository, CommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
    }

    public String getCommentNickname(boolean anonymous, AuthInfo authInfo, Post post) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        if (!anonymous) {
            return member.getNickname();
        }
        return getAnonymousNickname(post, member);
    }

    private String getAnonymousNickname(Post post, Member member) {
        if (post.isOwner(member.getId()) && post.isAnonymous()) {
            return post.getNickname();
        }
        return findPreviousAnonymousNicknameOrElseNewRandomNickname(post, member);
    }

    private String findPreviousAnonymousNicknameOrElseNewRandomNickname(Post post, Member member) {
        List<String> commentNicknamesByPostAndMember = commentRepository.findNickNamesByPostAndMember(post, member);
        return commentNicknamesByPostAndMember.stream()
                .filter(nickname -> !nickname.equals(member.getNickname()))
                .findFirst()
                .orElse(generateNewRandomNickname(post));
    }

    private String generateNewRandomNickname(Post post) {
        Set<String> alreadyUsedNickname = new HashSet<>(commentRepository.findNicknamesByPostId(post.getId()));
        alreadyUsedNickname.add(post.getNickname());
        return RandomNicknameGenerator.generate(alreadyUsedNickname);
    }
}
