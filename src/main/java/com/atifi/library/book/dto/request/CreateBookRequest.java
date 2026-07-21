package com.atifi.library.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBookRequest(
        @NotBlank
        String title,

        @NotBlank
        @Size(min = 13)
        String isbn,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        @PastOrPresent
        LocalDate publishedDate,

        @Positive
        Integer authorId
) {
}
