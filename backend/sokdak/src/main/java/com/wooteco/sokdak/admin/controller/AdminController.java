package com.wooteco.sokdak.admin.controller;

import com.wooteco.sokdak.admin.dto.EmailsAddRequest;
import com.wooteco.sokdak.admin.dto.PostReportsResponse;
import com.wooteco.sokdak.admin.dto.TicketRequest;
import com.wooteco.sokdak.admin.dto.TicketsResponse;
import com.wooteco.sokdak.admin.service.AdminService;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Void> success() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email")
    public ResponseEntity<Void> registerEmail(@Login AuthInfo authInfo,
                                              @RequestBody EmailsAddRequest emailsAddRequest) {
        adminService.registerEmail(authInfo, emailsAddRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @Login AuthInfo authInfo) {
        adminService.deletePost(id, authInfo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{id}/postreports/{postReportCount}")
    public ResponseEntity<Void> blockPost(@PathVariable Long id, @PathVariable Long postReportCount,
                                          @Login AuthInfo authInfo) {
        adminService.blockPost(id, postReportCount, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{id}/postreports")
    public ResponseEntity<Void> unblockPost(@PathVariable Long id, @Login AuthInfo authInfo) {
        adminService.unblockPost(id, authInfo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/postreports")
    public ResponseEntity<PostReportsResponse> findAllPostReports(@Login AuthInfo authInfo) {
        PostReportsResponse postReportsResponse = adminService.findAllPostReport(authInfo);
        return ResponseEntity.ok().body(postReportsResponse);
    }

    @GetMapping("/tickets")
    public ResponseEntity<TicketsResponse> findAllTickets(@Login AuthInfo authInfo) {
        TicketsResponse allTicket = adminService.findAllTickets(authInfo);
        return ResponseEntity.ok().body(allTicket);
    }

    @PostMapping("/tickets")
    public ResponseEntity<Void> saveTicket(@Login AuthInfo authInfo, @RequestBody TicketRequest ticketRequest) {
        adminService.saveTicket(authInfo, ticketRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/tickets")
    public ResponseEntity<Void> updateTicket(@Login AuthInfo authInfo, @RequestBody TicketRequest ticketRequest) {
        adminService.changeTicketUsedState(authInfo, ticketRequest);
        return ResponseEntity.ok().build();
    }
}
