package com.tu.libraryManagementSystemBackend.controller;

import com.tu.libraryManagementSystemBackend.dto.BookRequest;
import com.tu.libraryManagementSystemBackend.dto.BookResponse;
import com.tu.libraryManagementSystemBackend.model.Book;
import com.tu.libraryManagementSystemBackend.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id, @Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public ResponseEntity<List<BookResponse>> getAllBooks() {
//        List<BookResponse> books = bookService.getAllBooks();
//        return ResponseEntity.ok(books);
//    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(new Sort.Order(Sort.Direction.fromString(sort[1]), sort[0]));
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<BookResponse> response = bookService.getAllBooks(genre, maxPrice, search, pageable);

        return ResponseEntity.ok(response);
    }

    private List<Sort.Order> parseSort(String[] sort) {
        return Arrays.stream(sort)
                .map(s->s.split(","))
                .map(arr->new Sort.Order(Sort.Direction.fromString(arr[1]), arr[0]))
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        BookResponse book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }
}
