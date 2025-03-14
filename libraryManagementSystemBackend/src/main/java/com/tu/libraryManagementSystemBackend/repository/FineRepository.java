package com.tu.libraryManagementSystemBackend.repository;

import com.tu.libraryManagementSystemBackend.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FineRepository extends JpaRepository<Fine, UUID> {
    Optional<Fine> findByLoanId(UUID loanId);

    boolean existsByLoanId(UUID id);
}