package com.atifi.library.review.service;

import com.atifi.library.book.exception.BookNotFoundException;
import com.atifi.library.book.model.Book;
import com.atifi.library.book.repository.BookRepository;
import com.atifi.library.review.dto.request.CreateReviewRequest;
import com.atifi.library.review.dto.request.ReviewFilter;
import com.atifi.library.review.dto.request.UpdateReviewRequest;
import com.atifi.library.review.dto.response.ReviewResponse;
import com.atifi.library.review.exception.ReviewNotFoundException;
import com.atifi.library.review.mapper.ReviewMapper;
import com.atifi.library.review.model.Review;
import com.atifi.library.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.atifi.library.review.repository.ReviewSpecifications.commentContains;
import static com.atifi.library.review.repository.ReviewSpecifications.hasAuthorId;
import static com.atifi.library.review.repository.ReviewSpecifications.hasBookId;
import static com.atifi.library.review.repository.ReviewSpecifications.hasComment;
import static com.atifi.library.review.repository.ReviewSpecifications.minMaxRating;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public Page<ReviewResponse> findAll(ReviewFilter filters, Pageable pageable) {
        Specification<Review> spec = Specification.where(minMaxRating(filters.minRating(), filters.maxRating())
                .and(commentContains(filters.comment()))
                .and(hasBookId(filters.bookId()))
                .and(hasAuthorId(filters.authorId()))
                .and(hasComment(filters.hasComment()))
        );

        Page<Review> reviews = reviewRepository.findAll(spec, pageable);
        return reviews.map(ReviewMapper::toResponse);
    }

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
