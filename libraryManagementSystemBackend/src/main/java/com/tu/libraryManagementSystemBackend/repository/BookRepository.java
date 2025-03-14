package com.tu.libraryManagementSystemBackend.repository;

import com.tu.libraryManagementSystemBackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Optional<Book> findById(UUID id);
}