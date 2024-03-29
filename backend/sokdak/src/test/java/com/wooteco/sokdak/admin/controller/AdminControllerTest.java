package com.wooteco.sokdak.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.admin.dto.PostReportElement;
import com.wooteco.sokdak.admin.dto.PostReportsResponse;
import com.wooteco.sokdak.admin.dto.TicketElement;
import com.wooteco.sokdak.admin.dto.TicketsResponse;
import com.wooteco.sokdak.admin.exception.NoAdminException;
import com.wooteco.sokdak.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("관리자 컨트롤러 테스트")
class AdminControllerTest extends ControllerTest {

    @DisplayName("관리자는 게시글을 삭제한다.")
    @Test
    void deletePost() {
        doNothing()
                .when(adminService)
                .deletePost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer admin")
                .when().delete("admin/posts/" + 1L)
                .then().log().all()
                .apply(document("admin/post/delete/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("관리자가 아닐 경우 게시글을 삭제할 수 없다.")
    @Test
    void deletePost_Exception_NoAdmin() {
        doThrow(new NoAdminException())
                .when(adminService)
                .deletePost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer notAdmin")
                .when().delete("admin/posts/" + 1L)
                .then().log().all()
                .apply(document("admin/post/delete/fail/noAdmin"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("관리자는 게시글을 블락한다.")
    @Test
    void blockPost() {
        doNothing()
                .when(adminService)
                .blockPost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer admin")
                .when().post("admin/posts/1/postreports/5")
                .then().log().all()
                .apply(document("admin/post/add/postreports/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("관리자가 아닐 경우 게시글을 블락할 수 없다.")
    @Test
    void blockPost_Exception_NoAdmin() {
        doThrow(new NoAdminException())
                .when(adminService)
                .blockPost(any(), any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer noAdmin")
                .when().post("admin/posts/1/postreports/5")
                .then().log().all()
                .apply(document("admin/post/add/postreports/fail/noAdmin"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("관리자는 게시글 신고를 초기화한다.")
    @Test
    void unblockPost() {
        doNothing()
                .when(adminService)
                .unblockPost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer admin")
                .when().delete("admin/posts/1/postreports")
                .then().log().all()
                .apply(document("admin/post/delete/postreports/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("관리자가 아닐 경우 게시글 신고를 초기화할 수 없다.")
    @Test
    void unblockPost_Exception_NoAdmin() {
        doThrow(new NoAdminException())
                .when(adminService)
                .unblockPost(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer noAdmin")
                .when().delete("admin/posts/1/postreports")
                .then().log().all()
                .apply(document("admin/post/delete/postreports/fail/noAdmin"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("관리자는 게시글 신고 목록을 조회한다.")
    @Test
    void findAllPostReports() {
        PostReportElement postReportElement = PostReportElement.builder()
                .postId(1L)
                .reportMessage("비속어")
                .createdAt(LocalDateTime.now())
                .id(1L)
                .reporterId(1L)
                .build();
        PostReportElement postReportElement2 = PostReportElement.builder()
                .postId(1L)
                .reportMessage("비속어")
                .createdAt(LocalDateTime.now())
                .id(1L)
                .reporterId(2L)
                .build();
        List<PostReportElement> postReportElements = List.of(postReportElement, postReportElement2);
        doReturn(PostReportsResponse.of(postReportElements))
                .when(adminService)
                .findAllPostReports(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer admin")
                .when().get("admin/postreports")
                .then().log().all()
                .assertThat()
                .apply(document("admin/post/find/postreports/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닌 경우 게시글 신고 목록을 조회할 수 없다.")
    @Test
    void findAllPostReports_Exception_NoAdmin() {
        doThrow(new NoAdminException())
                .when(adminService)
                .findAllPostReports(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer noAdmin")
                .when().get("admin/postreports")
                .then().log().all()
                .assertThat()
                .apply(document("admin/post/find/postreports/fail/noAdmin"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("관리자는 모든 티켓들을 조회한다.")
    @Test
    void findAllTickets() {
        TicketElement ticketElement = TicketElement.builder()
                .id(1L)
                .serialNumber("123456")
                .used(false)
                .build();
        TicketElement ticketElement2 = TicketElement.builder()
                .id(2L)
                .serialNumber("234567")
                .used(false)
                .build();
        List<TicketElement> ticketElements = List.of(ticketElement, ticketElement2);
        doReturn(TicketsResponse.of(ticketElements))
                .when(adminService)
                .findAllTickets(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer admin")
                .when().get("admin/tickets")
                .then().log().all()
                .assertThat()
                .apply(document("admin/tickets/find/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닌 경우 모든 티켓들을 조회할 수 없다.")
    @Test
    void findAllTickets_Exception_NoAdmin() {
        doThrow(new NoAdminException())
                .when(adminService)
                .findAllTickets(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer noAdmin")
                .when().get("admin/tickets")
                .then().log().all()
                .assertThat()
                .apply(document("admin/tickets/find/fail/noAdmin"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("관리자는 티켓을 저장한다.")
    @Test
    void saveTicket() {
        TicketElement ticketElement = TicketElement.builder()
                .id(2L)
                .serialNumber("234567")
                .used(false)
                .build();
        doNothing()
                .when(adminService)
                .saveTicket(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer admin")
                .body(ticketElement)
                .when().post("admin/tickets")
                .then().log().all()
                .assertThat()
                .apply(document("admin/tickets/save/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닐 경우 티켓을 저장할 수 없다.")
    @Test
    void saveTicket_Exception_NoAdmin() {
        TicketElement ticketElement = TicketElement.builder()
                .id(2L)
                .serialNumber("234567")
                .used(false)
                .build();
        doThrow(new NoAdminException())
                .when(adminService)
                .saveTicket(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer noAdmin")
                .body(ticketElement)
                .when().post("admin/tickets")
                .then().log().all()
                .assertThat()
                .apply(document("admin/tickets/save/fail/noAdmin"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("관리자는 티켓을 수정한다.")
    @Test
    void updateTicket() {
        TicketElement ticketElement = TicketElement.builder()
                .id(2L)
                .serialNumber("234567")
                .used(false)
                .build();
        doNothing()
                .when(adminService)
                .changeTicketUsedState(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer admin")
                .body(ticketElement)
                .when().patch("admin/tickets")
                .then().log().all()
                .assertThat()
                .apply(document("admin/tickets/update/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닐 경우 티켓을 수정할 수 없다.")
    @Test
    void updateTicket_Exception_NoAdmin() {
        TicketElement ticketElement = TicketElement.builder()
                .id(2L)
                .serialNumber("234567")
                .used(false)
                .build();
        doThrow(new NoAdminException())
                .when(adminService)
                .changeTicketUsedState(any(), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer noAdmin")
                .body(ticketElement)
                .when().patch("admin/tickets")
                .then().log().all()
                .assertThat()
                .apply(document("admin/tickets/update/fail/noAdmin"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
