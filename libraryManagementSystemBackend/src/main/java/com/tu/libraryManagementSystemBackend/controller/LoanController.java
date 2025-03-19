package com.tu.libraryManagementSystemBackend.controller;

import com.tu.libraryManagementSystemBackend.dto.LoanRequest;
import com.tu.libraryManagementSystemBackend.dto.LoanResponse;
import com.tu.libraryManagementSystemBackend.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponse>> showAllLoans() {
        List<LoanResponse> response = loanService.getAllLoans();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponse>> showAllActiveLoans() {
        List<LoanResponse> response = loanService.getAllActiveLoans();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/returned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponse>> showAllReturnedLoans() {
        List<LoanResponse> response = loanService.getAllReturnedLoans();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> issueBook(@Valid @RequestBody LoanRequest request) {
        LoanResponse response = loanService.issueBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/return/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> returnBook(@PathVariable UUID id) {
        LoanResponse response = loanService.returnBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponse>> getUserLoans(@PathVariable UUID userId) {
        List<LoanResponse> response = loanService.getLoansByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{loanId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable UUID loanId) {
        LoanResponse response = loanService.getLoanById(loanId);
        return ResponseEntity.ok(response);
    }
}
