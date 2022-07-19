package com.wooteco.sokdak.post.domain;

import com.wooteco.sokdak.auth.exception.AuthenticationException;
import com.wooteco.sokdak.member.domain.Member;
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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @CreatedDate
    private LocalDateTime createdAt;

    protected Post() {
    }

    @Builder
    private Post(String title, String content, Member member) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getContent() {
        return content.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Member getMember() {
        return member;
    }

    public void updateTitle(String title, Long accessMemberId) {
        validateOwner(accessMemberId);
        this.title = new Title(title);
    }

    public void updateContent(String content, Long accessMemberId) {
        validateOwner(accessMemberId);
        this.content = new Content(content);
    }

    public void validateOwner(Long accessMemberId) {
        if (accessMemberId != member.getId()) {
            throw new AuthenticationException();
        }
    }
}
