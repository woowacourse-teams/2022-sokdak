package com.wooteco.sokdak.comment.domain;

import com.wooteco.sokdak.auth.exception.AuthenticationException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Message message;

    @CreatedDate
    private LocalDateTime createdAt;

    protected Comment() {
    }

    @Builder
    public Comment(Member member, Post post, String nickname, String message) {
        this.member = member;
        this.post = post;
        this.nickname = new Nickname(nickname);
        this.message = new Message(message);
    }

    public void validateOwner(Long accessMemberId) {
        if (accessMemberId != member.getId()) {
            throw new AuthenticationException();
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getMessage() {
        return message.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
