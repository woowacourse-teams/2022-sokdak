package com.wooteco.sokdak.like.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.like.domain.Like;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
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

    public static final int SPECIAL_BOARD_THRESHOLD = 5;

    private final BoardService boardService;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public LikeService(BoardService boardService, LikeRepository likeRepository,
                       PostRepository postRepository, MemberRepository memberRepository) {
        this.boardService = boardService;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public LikeFlipResponse flipLike(Long postId, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        flip(member, post);
        int likeCount = likeRepository.countByPostId(post.getId());
        boolean liked = likeRepository.existsByMemberIdAndPostId(member.getId(), post.getId());

        checkSpecialAndSave(likeCount, post);
        return new LikeFlipResponse(likeCount, liked);
    }

    private void checkSpecialAndSave(int likeCount, Post post) {
        if (likeCount >= SPECIAL_BOARD_THRESHOLD) {
            boardService.saveInSpecialBoard(post);
        }
    }

    private void flip(Member member, Post post) {
        Optional<Like> foundLike = likeRepository.findByMemberIdAndPostId(member.getId(), post.getId());
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
}
