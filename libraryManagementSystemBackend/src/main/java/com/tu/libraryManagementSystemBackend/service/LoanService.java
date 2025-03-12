package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.dto.LoanRequest;
import com.tu.libraryManagementSystemBackend.dto.LoanResponse;
import com.tu.libraryManagementSystemBackend.model.Book;
import com.tu.libraryManagementSystemBackend.model.Loan;
import com.tu.libraryManagementSystemBackend.model.User;
import com.tu.libraryManagementSystemBackend.repository.BookRepository;
import com.tu.libraryManagementSystemBackend.repository.LoanRepository;
import com.tu.libraryManagementSystemBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanResponse issueBook(LoanRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(()->new RuntimeException("User not found"));

        Book book = bookRepository.findById(request.bookId())
                .filter(b -> "AVAILABLE".equals(b.getStatus()))
                .orElseThrow(()->new RuntimeException("Book not available"));

        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status("ACTIVE")
                .build();

        book.setStatus("BORROWED");
        bookRepository.save(book);

        loan = loanRepository.save(loan);
        return convertToLoanResponse(loan);
    }

    private LoanResponse convertToLoanResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getUser().getId(),
                loan.getBook().getId(),
                loan.getCheckoutDate(),
                loan.getDueDate(),
                loan.getStatus()
        );
    }
}
