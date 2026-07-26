package com.atifi.library.review.dto.request;

public record ReviewFilter(
        Integer bookId,
        Integer minRating,
        Integer maxRating,
        String comment,
        Boolean hasComment,
        Integer authorId
) {
}
