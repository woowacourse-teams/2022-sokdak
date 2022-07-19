package com.wooteco.sokdak.util;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.wooteco.sokdak.auth.controller.AuthController;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.comment.controller.CommentController;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.member.controller.MemberController;
import com.wooteco.sokdak.member.service.EmailSender;
import com.wooteco.sokdak.member.service.EmailService;
import com.wooteco.sokdak.member.service.MemberService;
import com.wooteco.sokdak.post.controller.PostController;
import com.wooteco.sokdak.post.service.PostService;
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
        CommentController.class
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
