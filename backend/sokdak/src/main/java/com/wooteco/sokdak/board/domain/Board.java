package com.wooteco.sokdak.board.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "board")
    private List<PostBoard> postBoards = new ArrayList<>();

    private boolean userWritable = true;

    @CreatedDate
    private LocalDateTime createdAt;

    public Board() {
    }

    @Builder
    public Board(String name, boolean userWritable) {
        this.title = name;
        this.userWritable = userWritable;
    }
}
