package com.martinmadai.booklendingapp.domain.loan.repository;

import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.loan.model.Loan;
import com.martinmadai.booklendingapp.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByCopyAndReturnDateIsNull(BookCopy copy);

    List<Loan> findByUserAndReturnDateIsNull(User user);

    List<Loan> findAllByReturnDateIsNull(User user);

    Optional<Loan> findByCopyAndReturnDateIsNull(BookCopy copy);

    List<Loan> findByUser(User user);

    List<Loan> findByUserAndReturnDateIsNotNull(User user);

    List<Loan> findByUserUsernameOrderByLoanDateDesc(String username);

    List<Loan> findAllByOrderByLoanDateDesc();
}
