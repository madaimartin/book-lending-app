package com.martinmadai.booklendingapp.domain.loan.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LoanStatus {
    ACTIVE("ACT"),
    OVERDUE("OVD"),
    RETURNED("RTN");

    @Enumerated(EnumType.STRING)
    private final String code;
}