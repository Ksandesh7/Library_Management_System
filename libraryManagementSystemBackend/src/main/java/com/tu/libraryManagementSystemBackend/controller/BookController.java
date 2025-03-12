package com.tu.libraryManagementSystemBackend.controller;

import com.tu.libraryManagementSystemBackend.dto.BookRequest;
import com.tu.libraryManagementSystemBackend.dto.BookResponse;
import com.tu.libraryManagementSystemBackend.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.addBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
