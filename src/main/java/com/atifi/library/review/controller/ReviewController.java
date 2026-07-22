package com.atifi.library.review.controller;

import com.atifi.library.constants.ApiConstants;
import com.atifi.library.review.dto.request.CreateReviewRequest;
import com.atifi.library.review.dto.request.UpdateReviewRequest;
import com.atifi.library.review.dto.response.ReviewResponse;
import com.atifi.library.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.REVIEWS_BASE)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> findByBookId(@RequestParam() Integer bookId) {
        List<ReviewResponse> bookReviews = reviewService.findByBookId(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(bookReviews);
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> save(@Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse createdReview = reviewService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PutMapping(ApiConstants.PATH_ID)
    public ResponseEntity<ReviewResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request
    ) {
        ReviewResponse updatedReview = reviewService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
    }
}
