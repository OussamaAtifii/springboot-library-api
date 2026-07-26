package com.atifi.library.book.dto.request;

import java.math.BigDecimal;

public record BookFilter(
        String title,
        String isbn,
        Integer authorId,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}
