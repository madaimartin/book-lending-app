package com.martinmadai.booklendingapp.domain.bookcondition.service;

import com.martinmadai.booklendingapp.domain.bookcondition.exception.BookConditionNotFoundException;
import com.martinmadai.booklendingapp.domain.bookcondition.model.BookCondition;
import com.martinmadai.booklendingapp.domain.bookcondition.repository.BookConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookConditionService {

    private final BookConditionRepository bookConditionRepository;

    public BookCondition findConditionById(Long conditionId) {
        return bookConditionRepository.findById(conditionId)
                .orElseThrow(() -> new BookConditionNotFoundException(conditionId));
    }

    public List<BookCondition> findAll() {
        return bookConditionRepository.findAll();
    }
}
