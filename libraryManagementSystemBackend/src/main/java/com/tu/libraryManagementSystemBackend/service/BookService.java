package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.dto.BookRequest;
import com.tu.libraryManagementSystemBackend.dto.BookResponse;
import com.tu.libraryManagementSystemBackend.model.Book;
import com.tu.libraryManagementSystemBackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .status("AVAILABLE")
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
                book.getStatus()
        );
    }
}
