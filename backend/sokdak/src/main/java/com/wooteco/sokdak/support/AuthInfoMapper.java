package com.wooteco.sokdak.support;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import javax.servlet.http.HttpSession;

public class AuthInfoMapper {
    public AuthInfoMapper() {
    }

    public AuthInfo getAuthInfo(HttpSession session) {
        return (AuthInfo) session.getAttribute("member");
    }
}
