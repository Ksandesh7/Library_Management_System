package com.tu.libraryManagementSystemBackend.repository;

import com.tu.libraryManagementSystemBackend.dto.LoanResponse;
import com.tu.libraryManagementSystemBackend.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
    List<Loan> findByUserId(UUID userId);
    List<Loan> findByBookId(UUID bookId);
    List<Loan> findByStatus(String status);
    List<Loan> findByStatusAndDueDateBefore(String status, LocalDateTime dueDate);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.user.id = :userId AND l.book.id = :bookId AND l.status = :status")
    boolean existsByUserAndBookAndStatus(@Param("userId") UUID userId, @Param("bookId") UUID bookId, @Param("status") String status);

}