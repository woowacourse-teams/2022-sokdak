package com.wooteco.sokdak.post.domain;

import com.wooteco.sokdak.post.exception.InvalidPostException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    protected Post() {
    }

    @Builder
    private Post(String title, String content) {
        validate(title, content);
        this.title = title;
        this.content = content;
    }

    private void validate(String title, String content) {
        validateTitle(title);
        validateContent(content);
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidPostException();
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidPostException();
        }
    }
}
