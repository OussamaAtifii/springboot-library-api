package com.atifi.library.review.dto.response;

import lombok.Builder;

@Builder
public record ReviewResponse(
        Long id,
        Integer rating,
        String comment
) {
}
