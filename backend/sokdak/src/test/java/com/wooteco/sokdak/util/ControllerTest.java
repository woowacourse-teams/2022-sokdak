package com.wooteco.sokdak.util;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.wooteco.sokdak.auth.controller.AuthController;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.auth.service.RefreshTokenService;
import com.wooteco.sokdak.board.controller.BoardController;
import com.wooteco.sokdak.board.service.BoardService;
import com.wooteco.sokdak.comment.controller.CommentController;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.hashtag.controller.HashtagController;
import com.wooteco.sokdak.hashtag.service.HashtagService;
import com.wooteco.sokdak.like.controller.LikeController;
import com.wooteco.sokdak.like.service.LikeService;
import com.wooteco.sokdak.member.controller.MemberController;
import com.wooteco.sokdak.member.service.EmailSender;
import com.wooteco.sokdak.member.service.EmailService;
import com.wooteco.sokdak.member.service.MemberService;
import com.wooteco.sokdak.notification.controller.NotificationController;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.controller.PostController;
import com.wooteco.sokdak.post.service.PostService;
import com.wooteco.sokdak.report.controller.CommentReportController;
import com.wooteco.sokdak.report.controller.PostReportController;
import com.wooteco.sokdak.report.service.CommentReportService;
import com.wooteco.sokdak.report.service.PostReportService;
import com.wooteco.sokdak.support.AuthInterceptor;
import com.wooteco.sokdak.support.token.AuthenticationPrincipalArgumentResolver;
import com.wooteco.sokdak.support.token.TokenManager;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest({
        PostController.class,
        MemberController.class,
        AuthController.class,
        CommentController.class,
        LikeController.class,
        PostReportController.class,
        CommentReportController.class,
        BoardController.class,
        HashtagController.class,
        NotificationController.class
})
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    protected MockMvcRequestSpecification restDocs;

    @MockBean
    protected PostService postService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected EmailSender emailSender;

    @MockBean
    protected EmailService emailService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected LikeService likeService;

    @MockBean
    protected PostReportService postReportService;

    @MockBean
    protected CommentReportService commentReportService;

    @MockBean
    protected BoardService boardService;

    @MockBean
    protected HashtagService hashtagService;

    @MockBean
    protected NotificationService notificationService;

    @MockBean
    protected TokenManager tokenManager;

    @MockBean
    protected AuthInterceptor authInterceptor;

    @MockBean
    protected AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @MockBean
    protected RefreshTokenService refreshTokenService;

    @BeforeEach
    void setRestDocs(WebApplicationContext webApplicationContext,
                     RestDocumentationContextProvider restDocumentation) {

        restDocs = RestAssuredMockMvc.given()
                .mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .build())
                .log().all();
    }
}
