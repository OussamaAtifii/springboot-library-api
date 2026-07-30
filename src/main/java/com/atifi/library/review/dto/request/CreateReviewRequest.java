package com.atifi.library.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateReviewRequest(
        @NotNull()
        Integer bookId,

        @NotNull
        @Min(value = 1)
        @Max(value = 5)
        Integer rating,

        String comment
) {
}
