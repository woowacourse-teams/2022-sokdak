package com.wooteco.sokdak.post.domain;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.report.domain.PostReport;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
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

    private static final int BLOCKED_CONDITION = 5;
    private static final String BLIND_POST_MESSAGE = "블라인드 처리된 게시글입니다.";

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

    private String writerNickname;

    private String imageName;

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostHashtag> postHashtags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostBoard> postBoards = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostReport> postReports = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    protected Post() {
    }

    @Builder
    private Post(String title, String content, Member member, String writerNickname, List<PostLike> postLikes,
                 List<Comment> comments, List<PostHashtag> postHashtags, String imageName) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.member = member;
        this.postLikes = postLikes;
        this.writerNickname = writerNickname;
        this.comments = comments;
        this.postHashtags = postHashtags;
        this.imageName = imageName;
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

    public void updateImageName(String imageName, Long accessMemberId) {
        validateOwner(accessMemberId);
        this.imageName = imageName;
    }

    public void validateOwner(Long accessMemberId) {
        if (!Objects.equals(accessMemberId, member.getId())) {
            throw new AuthorizationException();
        }
    }

    public boolean isAuthorized(Long accessMemberId) {
        if (accessMemberId == null) {
            return false;
        }
        return member.getId().equals(accessMemberId);
    }

    public boolean isBlocked() {
        return postReports.size() >= BLOCKED_CONDITION;
    }

    public void addReport(PostReport other) {
        postReports.stream()
                .filter(other::isSameReporter)
                .findAny()
                .ifPresent(it -> {
                    throw new AlreadyReportPostException();
                });
        postReports.add(other);
    }

    public boolean isAnonymous() {
        return !getNickname().equals(member.getNickname());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        if (isBlocked()) {
            return BLIND_POST_MESSAGE;
        }
        return title.getValue();
    }

    public String getContent() {
        if (isBlocked()) {
            return BLIND_POST_MESSAGE;
        }
        return content.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Member getMember() {
        return member;
    }

    public int getLikeCount() {
        if (postLikes == null) {
            return 0;
        }
        return postLikes.size();
    }

    public int getCommentCount() {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }

    public List<PostLike> getPostLikes() {
        return postLikes;
    }

    public List<PostBoard> getPostBoards() {
        return postBoards;
    }

    public List<PostReport> getPostReports() {
        return postReports;
    }

    public String getNickname() {
        if (isBlocked()) {
            return BLIND_POST_MESSAGE;
        }
        return writerNickname;
    }

    public String getImageName() {
        return imageName;
    }
}
