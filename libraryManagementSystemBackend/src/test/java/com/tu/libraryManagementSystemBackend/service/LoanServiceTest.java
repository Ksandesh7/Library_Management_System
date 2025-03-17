package com.tu.libraryManagementSystemBackend.service;


import com.tu.libraryManagementSystemBackend.dto.LoanRequest;
import com.tu.libraryManagementSystemBackend.dto.LoanResponse;
import com.tu.libraryManagementSystemBackend.model.Book;
import com.tu.libraryManagementSystemBackend.model.Loan;
import com.tu.libraryManagementSystemBackend.model.User;
import com.tu.libraryManagementSystemBackend.repository.BookRepository;
import com.tu.libraryManagementSystemBackend.repository.LoanRepository;
import com.tu.libraryManagementSystemBackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FineService fineService;

    @InjectMocks
    private LoanService loanService;

    @Test
    void whenIssueBook_thenDecreaseQuantity() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        Book book = Book.builder().id(bookId).quantity(5).status("AVAILABLE").build();
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanResponse response = loanService.issueBook(new LoanRequest(userId, bookId));

        assertEquals(4, book.getQuantity()); // Quantity decreased by 1
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void whenReturnBook_thenUpdateStatus() {
        UUID loanId = UUID.randomUUID();
        Book book = Book.builder().quantity(0).status("UNAVAILABLE").build();
        Loan loan = Loan.builder().id(loanId).book(book).status("ACTIVE").build();

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanResponse response = loanService.returnBook(loanId);

        assertEquals("RETURNED", response.status());
        assertEquals(1, book.getQuantity()); // Quantity increased by 1
        assertEquals("AVAILABLE", book.getStatus());
    }
}
