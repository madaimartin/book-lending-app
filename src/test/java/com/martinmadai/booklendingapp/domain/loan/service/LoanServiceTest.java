package com.martinmadai.booklendingapp.domain.loan.service;

import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.bookcopy.service.BookCopyService;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import com.martinmadai.booklendingapp.domain.loan.dto.MyLoanDto;
import com.martinmadai.booklendingapp.domain.loan.exception.BookCopyNotAvailableException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanExtensionRepository loanExtensionRepository;

    @Mock
    private BookCopyService bookCopyService;

    @Mock
    private UserService userService;

    @Mock
    private MyLoanMapper myLoanMapper;

    @Mock
    private AdminLoanMapper adminLoanMapper;

    @InjectMocks
    private LoanService loanService;

    private User user;
    private BookCopy copy;
    private Loan loan;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("john")
                .build();

        copy = BookCopy.builder()
                .id(1L)
                .barcode("BC123")
                .status(BookStatus.AVAILABLE)
                .build();

        loan = Loan.builder()
                .id(1L)
                .user(user)
                .copy(copy)
                .loanDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusWeeks(3))
                .build();
    }

    @Test
    void loanBookCopy_success() {
        when(bookCopyService.findBookCopyById(1L)).thenReturn(copy);
        when(userService.findByUsername("john")).thenReturn(user);
        when(loanRepository.existsByCopyAndReturnDateIsNull(copy)).thenReturn(false);
        when(loanRepository.findByUserAndReturnDateIsNull(user)).thenReturn(List.of());

        loanService.loanBookCopy(1L, "john");

        assertEquals(BookStatus.LOANED, copy.getStatus());
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void loanBookCopy_copyNotAvailable_throwsException() {
        copy.setStatus(BookStatus.LOANED);

        when(bookCopyService.findBookCopyById(1L)).thenReturn(copy);
        when(userService.findByUsername("john")).thenReturn(user);

        assertThrows(BookCopyNotAvailableException.class, () -> loanService.loanBookCopy(1L, "john"));
    }

    @Test
    void loanBookCopy_existingActiveLoan_throwsException() {
        when(bookCopyService.findBookCopyById(1L)).thenReturn(copy);
        when(userService.findByUsername("john")).thenReturn(user);
        when(loanRepository.existsByCopyAndReturnDateIsNull(copy)).thenReturn(true);

        assertThrows(BookCopyNotAvailableException.class,
                () -> loanService.loanBookCopy(1L, "john"));
    }

    @Test
    void loanBookCopy_maxActiveLoansExceeded_throwsException() {
        when(bookCopyService.findBookCopyById(1L)).thenReturn(copy);
        when(userService.findByUsername("john")).thenReturn(user);
        when(loanRepository.existsByCopyAndReturnDateIsNull(copy)).thenReturn(false);
        when(loanRepository.findByUserAndReturnDateIsNull(user))
                .thenReturn(Collections.nCopies(LoanService.MAX_ACTIVE_LOANS, loan));

        assertThrows(MaxActiveLoansExceededException.class, () -> loanService.loanBookCopy(1L, "john"));
    }

    @Test
    void extendLoan_success() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(userService.findByUsername("john")).thenReturn(user);
        when(loanExtensionRepository.countByLoan(loan)).thenReturn(0L);

        LocalDateTime oldDueDate = loan.getDueDate();

        loanService.extendLoan(1L, "john");

        LocalDateTime expectedNewDueDate = oldDueDate.plusWeeks(LoanService.EXTEND_WEEK_LIMIT);

        assertEquals(expectedNewDueDate, loan.getDueDate());
        verify(loanExtensionRepository).save(any(LoanExtension.class));
    }


    @Test
    void extendLoan_notOwner_throwsException() {
        User otherUser = User.builder()
                .id(2L)
                .build();
        loan.setUser(otherUser);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(userService.findByUsername("john")).thenReturn(user);

        assertThrows(UnauthorizedLoanAccessException.class, () -> loanService.extendLoan(1L, "john"));
    }

    @Test
    void extendLoan_notActive_throwsException() {
        loan.setReturnDate(LocalDateTime.now());

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(userService.findByUsername("john")).thenReturn(user);

        assertThrows(BookCopyNotAvailableException.class, () -> loanService.extendLoan(1L, "john"));
    }

    @Test
    void extendLoan_extensionLimitReached_throwsException() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(userService.findByUsername("john")).thenReturn(user);
        when(loanExtensionRepository.countByLoan(loan)).thenReturn((long) LoanService.MAX_EXTENSIONS);

        assertThrows(MaxActiveLoansExceededException.class, () -> loanService.extendLoan(1L, "john"));
    }

    @Test
    void findMyLoans_mapsCorrectly() {
        when(loanRepository.findByUserUsernameOrderByLoanDateDesc("john")).thenReturn(List.of(loan));

        MyLoanDto dto = new MyLoanDto();
        when(myLoanMapper.toDto(loan)).thenReturn(dto);

        List<MyLoanDto> result = loanService.findMyLoans("john");

        assertEquals(1, result.size());
    }

    @Test
    void returnLoan_success() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        loanService.returnLoan(1L);

        assertNotNull(loan.getReturnDate());
        assertEquals(BookStatus.AVAILABLE, copy.getStatus());

        verify(bookCopyService).updateBookCopy(copy);
        verify(loanRepository).save(loan);
    }

    @Test
    void returnLoan_alreadyReturned_doesNothing() {
        loan.setReturnDate(LocalDateTime.now());

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        loanService.returnLoan(1L);

        verify(loanRepository, never()).save(any());
        verify(bookCopyService, never()).updateBookCopy(any());
    }

}
