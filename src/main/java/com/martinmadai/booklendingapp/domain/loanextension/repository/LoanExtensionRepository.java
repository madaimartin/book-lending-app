package com.martinmadai.booklendingapp.domain.loanextension.repository;

import com.martinmadai.booklendingapp.domain.loan.model.Loan;
import com.martinmadai.booklendingapp.domain.loanextension.model.LoanExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanExtensionRepository extends JpaRepository<LoanExtension, Long> {

    List<LoanExtension> findByLoan(Loan loan);

    long countByLoan(Loan loan);
}
