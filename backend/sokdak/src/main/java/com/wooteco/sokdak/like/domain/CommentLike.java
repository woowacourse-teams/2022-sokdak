package com.wooteco.sokdak.like.domain;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "comment_likes")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_likes_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected CommentLike() {
    }

    @Builder
    private CommentLike(Comment comment, Member member) {
        this.comment = comment;
        this.member = member;
        comment.addCommentLike(this);
    }

    public boolean isLikeOf(Long memberId) {
        return member.hasId(memberId);
    }
}
