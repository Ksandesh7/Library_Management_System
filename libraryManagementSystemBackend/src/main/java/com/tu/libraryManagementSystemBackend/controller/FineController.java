package com.tu.libraryManagementSystemBackend.controller;

import com.tu.libraryManagementSystemBackend.dto.FineResponse;
import com.tu.libraryManagementSystemBackend.dto.PaymentRequest;
import com.tu.libraryManagementSystemBackend.dto.PaymentResponse;
import com.tu.libraryManagementSystemBackend.service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {
    private final FineService fineService;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> payFine(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = fineService.payFine(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<FineResponse> getFineByLoanId(@PathVariable UUID loanId) {
        FineResponse response = fineService.getFineByLoanId(loanId);
        return ResponseEntity.ok(response);
    }

}
