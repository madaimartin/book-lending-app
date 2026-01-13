package com.martinmadai.booklendingapp.integration.loan;

import com.martinmadai.booklendingapp.domain.loan.model.Loan;
import com.martinmadai.booklendingapp.domain.loan.repository.LoanRepository;
import com.martinmadai.booklendingapp.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class LoanRepositoryIntegrationTest {

    @Autowired
    private LoanRepository loanRepository;

    /*
     * Integration Tests: coming soon...
     */

    /*
    @Autowired
    private TestEntityManager em;

    @Test
    void findByUserAndReturnDateIsNull_returnsOnlyActiveLoans() {
        User user = em.persist(User.builder().username("john").build());

        Loan activeLoan = em.persist(Loan.builder()
                .user(user)
                .returnDate(null)
                .build());

        Loan returnedLoan = em.persist(Loan.builder()
                .user(user)
                .returnDate(LocalDateTime.now())
                .build());

        em.flush();

        List<Loan> result = loanRepository.findByUserAndReturnDateIsNull(user);

        assertEquals(1, result.size());
        assertEquals(activeLoan.getId(), result.getFirst().getId());
    }

     */
}