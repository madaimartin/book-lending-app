package com.martinmadai.booklendingapp.domain.bookcondition.repository;

import com.martinmadai.booklendingapp.domain.bookcondition.model.BookCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookConditionRepository extends JpaRepository<BookCondition, Long> {
}
