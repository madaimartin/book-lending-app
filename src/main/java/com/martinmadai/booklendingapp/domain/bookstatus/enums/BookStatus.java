package com.martinmadai.booklendingapp.domain.bookstatus.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookStatus {
    AVAILABLE("AVL"),
    LOANED("LND"),
    LOST("LST"),
    DAMAGED("DMG");

    @Enumerated(EnumType.STRING)
    private final String code;
}