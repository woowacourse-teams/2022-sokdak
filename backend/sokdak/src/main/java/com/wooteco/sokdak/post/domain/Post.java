package com.wooteco.sokdak.post.domain;

import com.wooteco.sokdak.auth.exception.AuthenticationException;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.like.domain.Like;
import com.wooteco.sokdak.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @OneToMany(mappedBy = "post")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostHashtag> postHashtags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostBoard> postBoards = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    protected Post() {
    }

    @Builder
    private Post(String title, String content, Member member, List<Like> likes, List<Comment> comments,
                 List<PostHashtag> postHashtags) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.member = member;
        this.likes = likes;
        this.comments = comments;
        this.postHashtags = postHashtags;
    }

    public boolean isModified() {
        return !createdAt.equals(modifiedAt);
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
        if (!Objects.equals(accessMemberId, member.getId())) {
            throw new AuthenticationException();
        }
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

    public int getLikeCount() {
        return likes.size();
    }

    public int getCommentCount() {
        return comments.size();
    }

    public List<Like> getLikes() {
        return likes;
    }

    public List<PostHashtag> getPostHashtags() {
        return postHashtags;
    }

    public List<PostBoard> getPostBoards() {
        return postBoards;
    }
}
