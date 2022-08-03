package com.wooteco.sokdak.comment.domain;

import com.wooteco.sokdak.auth.exception.AuthenticationException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.domain.CommentReport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    private static final int BLOCKED_CONDITION = 5;

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

    @OneToMany(mappedBy = "comment")
    private List<CommentReport> commentReports = new ArrayList<>();

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

    public boolean isBlocked() {
        return commentReports.size() >= BLOCKED_CONDITION;
    }

    public boolean isAuthenticated(Long accessMemberId) {
        if (accessMemberId == null) {
            return false;
        }
        return member.getId().equals(accessMemberId);
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

    public List<CommentReport> getCommentReports() {
        return commentReports;
    }
}
