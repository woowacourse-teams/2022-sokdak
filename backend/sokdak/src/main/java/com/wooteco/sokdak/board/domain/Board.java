package com.wooteco.sokdak.board.domain;

import com.wooteco.sokdak.member.domain.RoleType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Column(name = "board_id")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "board")
    private List<PostBoard> postBoards = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @CreatedDate
    private LocalDateTime createdAt;

    public Board() {
    }

    @Builder
    public Board(String name, BoardType boardType) {
        this.title = name;
        this.boardType = boardType;
    }

    public boolean isUserWritable(String role) {
        if (RoleType.USER.getName().equals(role) && boardType == BoardType.NON_WRITABLE) {
            return false;
        }
        return true;
    }
}
