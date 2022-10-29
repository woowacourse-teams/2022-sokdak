package com.wooteco.sokdak.hashtag.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@BatchSize(size = 1000)
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    private String name;

    protected Hashtag() {
    }

    @Builder
    public Hashtag(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Hashtag)) {
            return false;
        }
        Hashtag hashtag = (Hashtag) o;
        return Objects.equals(getName(), hashtag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
