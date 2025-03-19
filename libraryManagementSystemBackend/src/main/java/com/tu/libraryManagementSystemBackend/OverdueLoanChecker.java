package com.tu.libraryManagementSystemBackend;

import com.tu.libraryManagementSystemBackend.model.Loan;
import com.tu.libraryManagementSystemBackend.repository.LoanRepository;
import com.tu.libraryManagementSystemBackend.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OverdueLoanChecker {
    private final LoanRepository loanRepository;
    private final FineService fineService;

    @Scheduled(fixedRate = 60_000)
    public void checkAndUpdateFines() {
        List<Loan> activeOverdueLoans = loanRepository.findByStatusAndDueDateBefore(
                "ACTIVE",
                LocalDateTime.now()
        );
        activeOverdueLoans.forEach(fineService::createOrUpdateFine);
    }
}
