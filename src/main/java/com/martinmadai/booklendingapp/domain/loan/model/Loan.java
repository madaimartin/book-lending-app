package com.martinmadai.booklendingapp.domain.loan.model;

import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.loan.enums.LoanStatus;
import com.martinmadai.booklendingapp.domain.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "copy_id")
    private BookCopy copy;

    private LocalDateTime loanDate;

    private LocalDateTime dueDate;

    private LocalDateTime returnDate;

    @Transient
    public LoanStatus getStatus() {
        if (returnDate != null) {
            return LoanStatus.RETURNED;
        }
        if (dueDate.isBefore(LocalDateTime.now())) {
            return LoanStatus.OVERDUE;
        }
        return LoanStatus.ACTIVE;
    }
}
