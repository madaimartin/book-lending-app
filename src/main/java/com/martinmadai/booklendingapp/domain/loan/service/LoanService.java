package com.martinmadai.booklendingapp.domain.loan.service;

import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.bookcopy.service.BookCopyService;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import com.martinmadai.booklendingapp.domain.loan.dto.AdminLoanDto;
import com.martinmadai.booklendingapp.domain.loan.dto.MyLoanDto;
import com.martinmadai.booklendingapp.domain.loan.enums.LoanStatus;
import com.martinmadai.booklendingapp.domain.loan.exception.BookCopyNotAvailableException;
import com.martinmadai.booklendingapp.domain.loan.exception.LoanNotFoundException;
import com.martinmadai.booklendingapp.domain.loan.exception.MaxActiveLoansExceededException;
import com.martinmadai.booklendingapp.domain.loan.exception.UnauthorizedLoanAccessException;
import com.martinmadai.booklendingapp.domain.loan.mapper.AdminLoanMapper;
import com.martinmadai.booklendingapp.domain.loan.mapper.MyLoanMapper;
import com.martinmadai.booklendingapp.domain.loan.model.Loan;
import com.martinmadai.booklendingapp.domain.loan.repository.LoanRepository;
import com.martinmadai.booklendingapp.domain.loanextension.model.LoanExtension;
import com.martinmadai.booklendingapp.domain.loanextension.repository.LoanExtensionRepository;
import com.martinmadai.booklendingapp.domain.user.model.User;
import com.martinmadai.booklendingapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    public static final int LOAN_WEEK_LIMIT = 3;
    public static final int EXTEND_WEEK_LIMIT = 2;
    public static final int MAX_ACTIVE_LOANS = 5;
    public static final int MAX_EXTENSIONS = 2;

    private final LoanRepository loanRepository;
    private final LoanExtensionRepository loanExtensionRepository;
    private final BookCopyService bookCopyService;
    private final UserService userService;
    private final MyLoanMapper myLoanMapper;
    private final AdminLoanMapper adminLoanMapper;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public void loanBookCopy(Long copyId, String username) {
        BookCopy copy = bookCopyService.findBookCopyById(copyId);
        User currentUser = userService.findByUsername(username);

        // Check the status
        if (copy.getStatus() != BookStatus.AVAILABLE) {
            throw new BookCopyNotAvailableException(copy.getBarcode());
        }

        // Verifying that loan does not already exist
        if (loanRepository.existsByCopyAndReturnDateIsNull(copy)) {
            throw new BookCopyNotAvailableException(copy.getBarcode());
        }

        if (getActiveLoansByUser(currentUser).size() >= MAX_ACTIVE_LOANS) {
            throw new MaxActiveLoansExceededException();
        }

        // Creating the loan
        Loan loan = Loan.builder()
                .user(currentUser)
                .copy(copy)
                .loanDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusWeeks(LOAN_WEEK_LIMIT))
                .build();

        // Update the status of the book copy
        copy.setStatus(BookStatus.LOANED);

        loanRepository.save(loan);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public void extendLoan(Long loanId, String username) {
        Loan loan = findLoanById(loanId);
        User currentUser = userService.findByUsername(username);

        // Every user can extend their own loans only
        if (!loan.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedLoanAccessException();
        }

        // Only active loans can be extended
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new BookCopyNotAvailableException(loan.getCopy().getBarcode());
        }

        if (loanExtensionRepository.countByLoan(loan) >= MAX_EXTENSIONS) {
            throw new MaxActiveLoansExceededException();
        }

        LocalDateTime oldDueDate = loan.getDueDate();
        LocalDateTime newDueDate = oldDueDate.plusWeeks(EXTEND_WEEK_LIMIT);

        loan.setDueDate(newDueDate);

        LoanExtension extension = LoanExtension.builder()
                .loan(loan)
                .extendedBy(currentUser)
                .extendedAt(LocalDateTime.now())
                .oldDueDate(oldDueDate)
                .newDueDate(newDueDate)
                .build();

        loanExtensionRepository.save(extension);
    }

    @Transactional(readOnly = true)
    public List<MyLoanDto> findMyLoans(String username) {
        return loanRepository.findByUserUsernameOrderByLoanDateDesc(username)
                .stream()
                .map(myLoanMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    public List<Loan> getActiveLoansByUser(User user) {
        return loanRepository.findByUserAndReturnDateIsNull(user);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional(readOnly = true)
    public List<AdminLoanDto> findAllLoans() {
        return loanRepository.findAllByOrderByLoanDateDesc()
                .stream()
                .map(adminLoanMapper::toDto)
                .toList();
    }

    @Transactional
    public void returnLoan(Long loanId) {
        Loan loan = findLoanById(loanId);

        if (loan.getReturnDate() != null) {
            return;
        }

        loan.setReturnDate(LocalDateTime.now());

        BookCopy copy = loan.getCopy();
        copy.setStatus(BookStatus.AVAILABLE);

        loanRepository.save(loan);
        bookCopyService.updateBookCopy(copy);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<Loan> getAllActiveLoans(User user) {
        return loanRepository.findAllByReturnDateIsNull(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<Loan> findOverdueLoans() {
        return loanRepository.findAll().stream()
                .filter(loan -> loan.getStatus() == LoanStatus.OVERDUE)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    public List<Loan> getLoanHistory(User user) {
        return loanRepository.findByUserAndReturnDateIsNotNull(user);
    }

    public Loan findLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
    }
}
