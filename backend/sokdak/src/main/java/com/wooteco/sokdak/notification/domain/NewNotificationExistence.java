package com.wooteco.sokdak.notification.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NewNotificationExistence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_notification_existence_id")
    private Long id;

    private Long memberId;

    private Boolean existence;

    protected NewNotificationExistence() {
    }

    public NewNotificationExistence(Long memberId, boolean existence) {
        this.memberId = memberId;
        this.existence = existence;
    }

    public Long getMemberId() {
        return memberId;
    }

    public boolean exists() {
        return existence;
    }
}
