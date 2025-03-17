package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.model.Config;
import com.tu.libraryManagementSystemBackend.model.Fine;
import com.tu.libraryManagementSystemBackend.model.Loan;
import com.tu.libraryManagementSystemBackend.repository.ConfigRepository;
import com.tu.libraryManagementSystemBackend.repository.FineRepository;
import com.tu.libraryManagementSystemBackend.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ConfigRepository configRepository;

    @InjectMocks
    private FineService fineService;

    @Test
    void whenCreateFine_thenCalculateCorrectAmount() {
        Loan loan = Loan.builder()
                .checkoutDate(LocalDateTime.now().minusDays(15))
                .dueDate(LocalDateTime.now().minusDays(1))
                .build();

        when(configRepository.findByKey("daily_fine_rate"))
                .thenReturn(Optional.of(new Config(UUID.randomUUID(), "daily_fine_rate", "0.50", null)));

        fineService.createFine(loan);

        ArgumentCaptor<Fine> fineCaptor = ArgumentCaptor.forClass(Fine.class);
        verify(fineRepository).save(fineCaptor.capture());

        assertEquals(new BigDecimal("7.00"), fineCaptor.getValue().getAmount()); // 14 days overdue * 0.50
    }
}