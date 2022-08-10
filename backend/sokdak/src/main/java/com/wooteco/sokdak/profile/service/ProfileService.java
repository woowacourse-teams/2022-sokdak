package com.wooteco.sokdak.profile.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.profile.dto.MyPostsResponse;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.profile.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.profile.exception.DuplicateNicknameException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfileService {

    public MemberRepository memberRepository;
    public PostRepository postRepository;

    public ProfileService(MemberRepository memberRepository,
                          PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    public MyPostsResponse findMyPosts(Pageable pageable, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Page<Post> posts = postRepository.findPostsByMemberOrderByCreatedAtDesc(pageable, member);
        return MyPostsResponse.ofPosts(posts.getContent(), posts.getTotalPages());
    }
}
