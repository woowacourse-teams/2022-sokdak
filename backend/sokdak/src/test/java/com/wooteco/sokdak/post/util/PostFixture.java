package com.wooteco.sokdak.post.util;

import com.wooteco.sokdak.post.dto.NewPostRequest;
import java.util.List;

public class PostFixture {

    public static final String VALID_POST_TITLE = "제목";
    public static final String VALID_POST_CONTENT = "본문";

    public static final String UPDATED_POST_TITLE = "변경된 제목";
    public static final String UPDATED_POST_CONTENT = "변경된 본문";

    public static final NewPostRequest NEW_POST_REQUEST =
            new NewPostRequest("제목", "본문", false, List.of("태그1", "태그2"));
}
