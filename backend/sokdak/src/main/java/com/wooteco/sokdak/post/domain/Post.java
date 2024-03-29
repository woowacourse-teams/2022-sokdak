package com.wooteco.sokdak.post.domain;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.like.domain.PostLike;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.report.domain.PostReport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private static final Long HOT_BOARD_ID = 1L;

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

    private int viewCount = 0;

    private int likeCount = 0;

    private String writerNickname;

    private String imageName;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostHashtag> postHashtags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostBoard> postBoards = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "postId")
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

    public void updateTitle(String title) {
        this.title = new Title(title);
    }

    public void updateContent(String content) {
        this.content = new Content(content);
    }

    public void updateImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isOwner(Long accessMemberId) {
        if (accessMemberId == null) {
            return false;
        }
        return member.getId().equals(accessMemberId);
    }

    public boolean isBlocked() {
        return postReports.size() >= BLOCKED_CONDITION;
    }

    public void addReport(PostReport other) {
        postReports.add(other);
    }

    public boolean isAnonymous() {
        return !getNickname().equals(member.getNickname());
    }

    public boolean hasReportByMember(Member reporter) {
        return postReports.stream()
                .anyMatch(report -> report.isOwner(reporter));
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
        return likeCount;
    }

    public int getCommentCount() {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }

    public Board getWritableBoard() {
        return postBoards.stream()
                .map(PostBoard::getBoard)
                .filter(board -> board.isUserWritable("user"))
                .findFirst()
                .orElseThrow();
    }

    public List<PostLike> getPostLikes() {
        return postLikes;
    }

    public List<PostBoard> getPostBoards() {
        return postBoards;
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

    public List<PostReport> getPostReports() {
        return postReports;
    }

    public void deleteAllReports() {
        postReports = new ArrayList<>();
    }

    public void addPostLike(PostLike postLike) {
        postLikes.add(postLike);
    }

    public void deleteLike(PostLike postLike) {
        postLikes.remove(postLike);
        postLike.delete();
    }

    public Long getBoardId() {
        return postBoards.stream()
                .map(PostBoard::getBoard)
                .map(Board::getId)
                .filter(id -> id != HOT_BOARD_ID)
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    public int getViewCount() {
        return viewCount;
    }
}
