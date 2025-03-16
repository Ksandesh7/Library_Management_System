package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.dto.LoanRequest;
import com.tu.libraryManagementSystemBackend.dto.LoanResponse;
import com.tu.libraryManagementSystemBackend.exception.InvalidOperationException;
import com.tu.libraryManagementSystemBackend.exception.ResourceNotFoundException;
import com.tu.libraryManagementSystemBackend.model.Book;
import com.tu.libraryManagementSystemBackend.model.Loan;
import com.tu.libraryManagementSystemBackend.model.User;
import com.tu.libraryManagementSystemBackend.repository.BookRepository;
import com.tu.libraryManagementSystemBackend.repository.LoanRepository;
import com.tu.libraryManagementSystemBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineService fineService;

    public LoanResponse issueBook(LoanRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        Book book = bookRepository.findById(request.bookId())
                .filter(b -> "AVAILABLE".equals(b.getStatus()) && b.getQuantity()>0)
                .orElseThrow(()->new InvalidOperationException("Book not available"));

        if(book.getQuantity() <= 0) {
            throw new InvalidOperationException("Book out of stock");
        }

        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .checkoutDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusMinutes(1))
                .status("ACTIVE")
                .build();

        book.setQuantity(book.getQuantity()-1);
        if(book.getQuantity()==0) {
            book.setStatus("BORROWED");
        }

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

    public LoanResponse returnBook(UUID loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(()->new ResourceNotFoundException("Loan not found"));

        if("RETURNED".equals(loan.getStatus())) {
            throw new InvalidOperationException("Book already returned");
        }

        loan.setReturnDate(LocalDateTime.now());
        loan.setStatus("RETURNED");

        Book book = loan.getBook();
        book.setQuantity(book.getQuantity()+1);
        if(book.getQuantity()>0) {
            book.setStatus("AVAILABLE");
        }
        bookRepository.save(book);

        loan = loanRepository.save(loan);

        if(LocalDateTime.now().isAfter(loan.getDueDate())) {
            fineService.createFine(loan);
        }
        return convertToLoanResponse(loan);
    }

    public List<LoanResponse> getLoansByUserId(UUID userId) {
        return loanRepository.findByUserId(userId)
                .stream()
                .map(this::convertToLoanResponse)
                .toList();
    }

    public List<LoanResponse> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream()
                .map(this::convertToLoanResponse)
                .toList();
    }

    public List<LoanResponse> getAllActiveLoans() {
        List<Loan> activeLoans = loanRepository.findByStatus("ACTIVE");
        return activeLoans.stream()
                .map(this::convertToLoanResponse)
                .toList();
    }

    public List<LoanResponse> getAllReturnedLoans() {
        List<Loan> returnedLoans = loanRepository.findByStatus("RETURNED");
        return returnedLoans.stream()
                .map(this::convertToLoanResponse)
                .toList();
    }

}
