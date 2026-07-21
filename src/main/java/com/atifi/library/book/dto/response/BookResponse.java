package com.atifi.library.book.dto.response;

import com.atifi.library.author.dto.response.AuthorResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BookResponse(
        Integer id,
        String title,
        String isbn,
        BigDecimal price,
        LocalDate publishedDate,
        AuthorResponse author
) {
}
