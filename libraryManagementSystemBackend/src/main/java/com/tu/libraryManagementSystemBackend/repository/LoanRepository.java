package com.tu.libraryManagementSystemBackend.repository;

import com.tu.libraryManagementSystemBackend.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
    List<Loan> findByUserId(UUID userId);
    List<Loan> findByBookId(UUID bookId);
    List<Loan> findByStatus(String status);
}