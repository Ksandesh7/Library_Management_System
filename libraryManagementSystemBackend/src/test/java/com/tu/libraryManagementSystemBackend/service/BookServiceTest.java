package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.dto.BookRequest;
import com.tu.libraryManagementSystemBackend.dto.BookResponse;
import com.tu.libraryManagementSystemBackend.model.Book;
import com.tu.libraryManagementSystemBackend.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void whenNewBook_thenSaveBook() {
        BookRequest request = new BookRequest("1984", "9780451524935", "George Orwell", "Dystopian", 10, BigDecimal.valueOf(9.99), "image.jpg");
        Book savedBook = Book.builder().id(UUID.randomUUID()).isbn("9780451524935").status("AVAILABLE").build();

        when(bookRepository.existsByIsbn("9780451524935")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponse response = bookService.addBook(request);
        assertEquals("9780451524935", response.isbn());
        assertEquals("AVAILABLE", response.status());
    }

    @Test
    void whenDuplicateIsbn_thenThrowException() {
        BookRequest request = new BookRequest("1984", "9780451524935", "George Orwell", "Dystopian", 10, BigDecimal.valueOf(9.99), "image.jpg");
        when(bookRepository.existsByIsbn("9780451524935")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, ()->bookService.addBook(request));
    }

    @Test
    void whenUpdateBook_thenReturnUpdated() {
        UUID bookId = UUID.randomUUID();
        Book existingBook = Book.builder().id(bookId).isbn("oldIsbn").build();
        BookRequest updatedRequest = new BookRequest("Updated Title", "newIsbn", "New Author", "Sci-Fi", 5, BigDecimal.valueOf(19.99), "newImage.jpg");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        BookResponse response = bookService.updateBook(bookId, updatedRequest);
        assertEquals("newIsbn", response.isbn());
    }
}
