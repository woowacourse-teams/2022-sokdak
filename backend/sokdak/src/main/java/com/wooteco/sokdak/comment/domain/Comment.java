package com.wooteco.sokdak.comment.domain;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.like.domain.CommentLike;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.domain.CommentReport;
import com.wooteco.sokdak.report.exception.AlreadyReportCommentException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private static final String BLIND_COMMENT_MESSAGE = "블라인드 처리된 댓글입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<CommentReport> commentReports = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    private String nickname;

    @Embedded
    private Message message;

    private boolean softRemoved;

    @CreatedDate
    private LocalDateTime createdAt;

    protected Comment() {
    }

    @Builder
    public Comment(Member member, Post post, String nickname, String message, Comment parent) {
        this.member = member;
        this.post = post;
        this.nickname = nickname;
        this.message = new Message(message);
        this.parent = parent;
    }

    public static Comment parent(Member member, Post post, String nickname, String message) {
        return new Comment(member, post, nickname, message, null);
    }

    public static Comment child(Member member, Post post, String nickname, String message, Comment parent) {
        Comment child = new Comment(member, post, nickname, message, parent);
        parent.getChildren().add(child);
        return child;
    }

    public void validateOwner(Long accessMemberId) {
        if (!accessMemberId.equals(member.getId())) {
            throw new AuthorizationException();
        }
    }

    public boolean isPostWriter() {
        return post.getMember().equals(member);
    }

    public boolean isBlocked() {
        return commentReports.size() >= BLOCKED_CONDITION;
    }

    public boolean isAuthorized(Long accessMemberId) {
        if (accessMemberId == null) {
            return false;
        }
        return member.getId().equals(accessMemberId);
    }

    public void addReport(CommentReport other) {
        commentReports.add(other);
    }

    public boolean hasReportByMember(Member reporter) {
        return commentReports.stream()
                .anyMatch(report -> report.isOwner(reporter));
    }

    public Long getId() {
        return id;
    }

    public Comment getParent() {
        return parent;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public Member getMember() {
        return member;
    }

    public Post getPost() {
        return post;
    }

    public String getNickname() {
        if (isBlocked()) {
            return BLIND_COMMENT_MESSAGE;
        }
        return nickname;
    }

    public String getMessage() {
        if (isBlocked()) {
            return BLIND_COMMENT_MESSAGE;
        }
        return message.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<CommentReport> getCommentReports() {
        return commentReports;
    }

    public List<CommentLike> getCommentLikes() {
        return commentLikes;
    }

    public int getCommentLikesCount() {
        return commentLikes.size();
    }

    public boolean isSoftRemoved() {
        return softRemoved;
    }

    public void changePretendingToBeRemoved() {
        this.softRemoved = true;
    }

    public void deleteChild(Comment reply) {
        children.remove(reply);
    }

    public boolean isParent() {
        return Objects.isNull(parent);
    }

    public boolean hasNoReply() {
        return children.isEmpty();
    }
}
