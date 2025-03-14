package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.dto.FineResponse;
import com.tu.libraryManagementSystemBackend.dto.PaymentRequest;
import com.tu.libraryManagementSystemBackend.dto.PaymentResponse;
import com.tu.libraryManagementSystemBackend.exception.InvalidOperationException;
import com.tu.libraryManagementSystemBackend.exception.ResourceNotFoundException;
import com.tu.libraryManagementSystemBackend.model.Fine;
import com.tu.libraryManagementSystemBackend.model.Loan;
import com.tu.libraryManagementSystemBackend.model.Payment;
import com.tu.libraryManagementSystemBackend.repository.ConfigRepository;
import com.tu.libraryManagementSystemBackend.repository.FineRepository;
import com.tu.libraryManagementSystemBackend.repository.LoanRepository;
import com.tu.libraryManagementSystemBackend.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FineService {
    private final FineRepository fineRepository;
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final ConfigRepository configRepository;

    public PaymentResponse payFine(@Valid PaymentRequest request) {
        Fine fine = fineRepository.findById(request.fineId())
                .orElseThrow(()->new ResourceNotFoundException("Fine not found"));

        //TODO: Integrate later with Payment Gateway
        Payment payment = Payment.builder()
                .fine(fine)
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .transactionId(UUID.randomUUID().toString()) //TODO: Replace with real transaction Id
                .status("COMPLETED")
                .createdAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        if(payment.getAmount().compareTo(fine.getAmount())>=0) {
            fine.setStatus("PAID");
            fineRepository.save(fine);
        }

        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getStatus()
        );
    }

    public FineResponse getFineByLoanId(UUID loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(()->new ResourceNotFoundException("Loan not found"));

        Fine fine = fineRepository.findByLoanId(loanId)
                .orElseThrow(()->new ResourceNotFoundException("No fine for this loan"));

        return convertToFineResponse(fine);
    }

    private FineResponse convertToFineResponse(Fine fine) {
        return new FineResponse(
                fine.getId(),
                fine.getAmount(),
                fine.getStatus()
        );
    }

    public void createFine(Loan loan) {
        if(LocalDateTime.now().isAfter(loan.getDueDate())) {
            if(fineRepository.existsByLoanId(loan.getId())) {
                throw new InvalidOperationException("Fine already exists for this loan");
            }

            BigDecimal dailyFineRate = configRepository.findByKey("daily_fine_rate")
                    .map(config -> new BigDecimal(config.getValue()))
                    .orElseThrow(()->new InvalidOperationException("Fine rate not configured"));

            long daysOverdue = ChronoUnit.MINUTES.between(loan.getDueDate(), LocalDateTime.now());

            BigDecimal amount = dailyFineRate.multiply(BigDecimal.valueOf(daysOverdue));

            Fine fine = Fine.builder()
                    .loan(loan)
                    .amount(amount)
                    .status("UNPAID")
                    .build();

            fineRepository.save(fine);
        }
    }
}
