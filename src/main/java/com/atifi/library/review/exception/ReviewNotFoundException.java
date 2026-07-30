package com.atifi.library.review.exception;

import com.atifi.library.common.exception.ResourceNotFoundException;

public class ReviewNotFoundException extends ResourceNotFoundException {
    public ReviewNotFoundException(Long id) {
        super("Review with id " + id + " not found");
    }
}
