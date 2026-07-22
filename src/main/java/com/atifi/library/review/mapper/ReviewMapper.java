package com.atifi.library.review.mapper;

import com.atifi.library.book.model.Book;
import com.atifi.library.review.dto.request.CreateReviewRequest;
import com.atifi.library.review.dto.request.UpdateReviewRequest;
import com.atifi.library.review.dto.response.ReviewResponse;
import com.atifi.library.review.model.Review;

public class ReviewMapper {
    public static ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }

    public static Review toEntity(CreateReviewRequest request, Book book) {
        return Review.builder()
                .rating(request.rating())
                .comment(request.comment())
                .book(book)
                .build();
    }

    public static void updateEntity(Review review, UpdateReviewRequest request) {
        review.setRating(request.rating());
        review.setComment(request.comment());
    }

}
