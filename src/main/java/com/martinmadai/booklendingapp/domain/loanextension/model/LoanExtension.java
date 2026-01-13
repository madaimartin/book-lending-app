package com.martinmadai.booklendingapp.domain.loanextension.model;

import com.martinmadai.booklendingapp.domain.loan.model.Loan;
import com.martinmadai.booklendingapp.domain.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Loan loan;

    @ManyToOne(optional = false)
    private User extendedBy;

    private LocalDateTime extendedAt;

    private LocalDateTime oldDueDate;

    private LocalDateTime newDueDate;
}
