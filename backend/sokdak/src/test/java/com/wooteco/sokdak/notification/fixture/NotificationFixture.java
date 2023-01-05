package com.wooteco.sokdak.notification.fixture;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class NotificationFixture {

    public static final Pageable ZERO_PAGE_TWO_SIZE_CREATE_AT_DESCENDING_PAGEABLE =
            PageRequest.of(0, 2, Sort.by("createdAt").descending());
}
