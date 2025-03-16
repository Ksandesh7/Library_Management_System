package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.dto.BookRequest;
import com.tu.libraryManagementSystemBackend.dto.BookResponse;
import com.tu.libraryManagementSystemBackend.exception.ResourceNotFoundException;
import com.tu.libraryManagementSystemBackend.model.Book;
import com.tu.libraryManagementSystemBackend.repository.BookRepository;
import com.tu.libraryManagementSystemBackend.specifications.BookSpecifications;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookResponse addBook(BookRequest request) {
        if(bookRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("ISBN already exits");
        }

        Book book = Book.builder()
                .title(request.title())
                .isbn(request.isbn())
                .author(request.author())
                .genre(request.genre())
                .quantity(request.quantity())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .status(request.quantity() > 0 ? "AVAILABLE":"UNAVAILABLE")
                .build();

        book = bookRepository.save(book);
        return convertToBookResponse(book);
    }

    private BookResponse convertToBookResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthor(),
                book.getStatus(),
                book.getGenre(),
                book.getQuantity(),
                book.getPrice(),
                book.getImageUrl()
        );
    }

    public BookResponse updateBook(UUID id, @Valid BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Book not found"));

        if(!book.getIsbn().equals(request.isbn()) && bookRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("ISBN already exists");
        }

        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setQuantity(request.quantity());
        book.setPrice(request.price());
        book.setImageUrl(request.imageUrl());
        book.setStatus(request.quantity() > 0 ? "AVAILABLE":"UNAVAILABLE");

        book = bookRepository.save(book);
        return convertToBookResponse(book);
    }

    public void deleteBook(UUID id) {
        bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book not found"));
        bookRepository.deleteById(id);
    }

//    public List<BookResponse> getAllBooks() {
//        return bookRepository.findAll()
//                .stream()
//                .map(this::convertToBookResponse)
//                .toList();
//    }

    public Page<BookResponse> getAllBooks(
            String genre,
            BigDecimal maxPrice,
            String searchTerm,
            Pageable pageable
    ) {
        Specification<Book> spec = BookSpecifications.withFilters(genre, maxPrice, searchTerm);
        Page<Book> books = bookRepository.findAll(spec, pageable);
        return books.map(this::convertToBookResponse);
    }

    public BookResponse getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Book not found"));
        return convertToBookResponse(book);
    }

    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(()->new ResourceNotFoundException("Book not found"));
        return convertToBookResponse(book);
    }
}
