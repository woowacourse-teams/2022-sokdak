package com.wooteco.sokdak.post.domain;

import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ViewCount {

    private int viewCount = 0;

    public ViewCount(){
    }

    public void addViewCount() {
        viewCount += 1;
    }
}
