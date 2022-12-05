package com.wooteco.sokdak.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.wooteco.sokdak.auth.domain.encryptor.Encryptor;
import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CommentNicknameGeneratorTest {

    private final MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private final CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private final CommentNicknameGenerator commentNicknameGenerator =
            new CommentNicknameGenerator(memberRepository, commentRepository);

    private static final EncryptorI ENCRYPTOR = new Encryptor();
    private static final Member MEMBER1 = Member.builder()
            .id(1L)
            .nickname(new Nickname("닉네임1"))
            .password(Password.of(ENCRYPTOR, "Abcd123!@"))
            .username(Username.of(ENCRYPTOR, "first123"))
            .build();
    private static final Member MEMBER2 = Member.builder()
            .id(2L)
            .nickname(new Nickname("닉네임2"))
            .password(Password.of(ENCRYPTOR, "Abcd123!@"))
            .username(Username.of(ENCRYPTOR, "second123"))
            .build();
    private static final Post ANONYMOUS_POST = Post.builder()
            .member(MEMBER1)
            .title("제목")
            .content("본문")
            .writerNickname("익명닉네임")
            .build();
    private static final AuthInfo AUTH_INFO1 = new AuthInfo(1L, "USER", "abc1");
    private static final AuthInfo AUTH_INFO2 = new AuthInfo(2L, "USER", "abc2");

    @DisplayName("기명으로 댓글을 작성하면 회원의 닉네임을 반환한다.")
    @Test
    void getCommentNickname_Identified() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(MEMBER1));

        String actual = commentNicknameGenerator.getCommentNickname(false, AUTH_INFO1, ANONYMOUS_POST);

        assertThat(actual).isEqualTo(MEMBER1.getNickname());
    }

    @DisplayName("익명 게시글의 작성자가 익명의 댓글을 작성하면 게시글 작성자 닉네임과 동일한 닉네임을 반환한다.")
    @Test
    void getCommentNickname_Anonymous_PostWriter() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(MEMBER1));

        String actual = commentNicknameGenerator.getCommentNickname(true, AUTH_INFO1, ANONYMOUS_POST);

        assertThat(actual).isEqualTo(ANONYMOUS_POST.getNickname());
    }

    @DisplayName("게시글 작성자가 아닌 회원이 처음으로 익명의 댓글을 작성하면 랜덤 닉네임을 반환한다.")
    @Test
    void getCommentNickname_Anonymous_NotPostWriter_FirstComment() {
        when(memberRepository.findById(2L))
                .thenReturn(Optional.of(MEMBER2));

        String actual = commentNicknameGenerator.getCommentNickname(true, AUTH_INFO2, ANONYMOUS_POST);

        assertThat(actual).isNotEqualTo(MEMBER2.getNickname())
                .isNotEqualTo(ANONYMOUS_POST.getNickname());
    }

    @DisplayName("이전에 익명으로 댓글을 작성했던 게시글 작성자가 아닌 회원이 익명의 댓글을 작성하면 이전의 랜덤닉네임을 반환한다.")
    @Test
    void getCommentNickname_Anonymous_NotPostWriter_SecondComment() {
        when(memberRepository.findById(2L))
                .thenReturn(Optional.of(MEMBER2));
        when(commentRepository.findNickNamesByPostAndMember(ANONYMOUS_POST, MEMBER2))
                .thenReturn(List.of("이전의 닉네임"));

        String actual = commentNicknameGenerator.getCommentNickname(true, AUTH_INFO2, ANONYMOUS_POST);

        assertThat(actual).isEqualTo("이전의 닉네임");
    }

    @DisplayName("이전에 기명으로 댓글을 작성했던 게시글 작성자가 아닌 회원이 익명의 댓글을 작성하면 랜덤닉네임을 반환한다.")
    @Test
    void a() {
        when(memberRepository.findById(2L))
                .thenReturn(Optional.of(MEMBER2));
        when(commentRepository.findNickNamesByPostAndMember(ANONYMOUS_POST, MEMBER2))
                .thenReturn(List.of("닉네임2"));

        String actual = commentNicknameGenerator.getCommentNickname(true, AUTH_INFO2, ANONYMOUS_POST);

        assertThat(actual).isNotEqualTo("닉네임2");
    }
}
