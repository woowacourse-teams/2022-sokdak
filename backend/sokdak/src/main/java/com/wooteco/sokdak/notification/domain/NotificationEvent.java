package com.wooteco.sokdak.notification.domain;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;

public class NotificationEvent {

    private NotificationType notificationType;
    private Post post;
    private Member member;
    private Comment comment;
    private Comment reply;

    private NotificationEvent(NotificationType notificationType, Post post,
                              Member member, Comment comment, Comment reply) {
        this.notificationType = notificationType;
        this.post = post;
        this.member = member;
        this.comment = comment;
        this.reply = reply;
    }

    public static NotificationEvent newComment(Post post, Member member, Comment comment) {
        return new NotificationEvent(NotificationType.NEW_COMMENT, post, member, comment, null);
    }

    public static NotificationEvent newReply(Post post, Member member, Comment comment, Comment reply) {
        return new NotificationEvent(NotificationType.NEW_REPLY, post, member, comment, reply);
    }

    public static NotificationEvent postReport(Post post, Member member) {
        return new NotificationEvent(NotificationType.POST_REPORT, post, member, null, null);
    }

    public static NotificationEvent commentReport(Post post, Member member, Comment comment) {
        return new NotificationEvent(NotificationType.COMMENT_REPORT, post, member, comment, null);
    }

    public static NotificationEvent hotBoard(Post post, Member member) {
        return new NotificationEvent(NotificationType.HOT_BOARD, post, member, null, null);
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public Post getPost() {
        return post;
    }

    public Member getMember() {
        return member;
    }

    public Comment getComment() {
        return comment;
    }

    public Comment getReply() {
        return reply;
    }
}

