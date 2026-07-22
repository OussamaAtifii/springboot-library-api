package com.atifi.library.review.service;

import com.atifi.library.book.exception.BookNotFoundException;
import com.atifi.library.book.model.Book;
import com.atifi.library.book.repository.BookRepository;
import com.atifi.library.review.dto.request.CreateReviewRequest;
import com.atifi.library.review.dto.request.UpdateReviewRequest;
import com.atifi.library.review.dto.response.ReviewResponse;
import com.atifi.library.review.exception.ReviewNotFoundException;
import com.atifi.library.review.mapper.ReviewMapper;
import com.atifi.library.review.model.Review;
import com.atifi.library.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public List<ReviewResponse> findByBookId(Integer id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        List<Review> bookReviews = reviewRepository.findByBookId(id);
        return bookReviews.stream().map(ReviewMapper::toResponse).toList();
    }

    public ReviewResponse save(CreateReviewRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new BookNotFoundException(request.bookId()));

        Review review = ReviewMapper.toEntity(request, book);
        Review createdReview = reviewRepository.save(review);

        return ReviewMapper.toResponse(createdReview);
    }

    public ReviewResponse update(Long id, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        ReviewMapper.updateEntity(review, request);

        Review updatedReview = reviewRepository.save(review);
        return ReviewMapper.toResponse(updatedReview);
    }

}
