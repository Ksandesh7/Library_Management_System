package com.tu.libraryManagementSystemBackend.controller;

import com.tu.libraryManagementSystemBackend.dto.LoanRequest;
import com.tu.libraryManagementSystemBackend.dto.LoanResponse;
import com.tu.libraryManagementSystemBackend.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> issueBook(@Valid @RequestBody LoanRequest request) {
        LoanResponse response = loanService.issueBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
