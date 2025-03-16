package com.tu.libraryManagementSystemBackend.repository;

import com.tu.libraryManagementSystemBackend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Optional<Book> findById(UUID id);
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}