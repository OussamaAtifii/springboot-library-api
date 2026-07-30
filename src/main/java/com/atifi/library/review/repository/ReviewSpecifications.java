package com.atifi.library.review.repository;

import com.atifi.library.review.model.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecifications {
    public static Specification<Review> minMaxRating(Integer minRating, Integer maxRating) {
        return (root, query, cb) -> {
            if (minRating == null && maxRating == null) return null;

            if (minRating != null && maxRating != null) {
                return cb.between(root.get("rating"), minRating, maxRating);
            }

            if (minRating != null) {
                return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
            }

            return cb.lessThanOrEqualTo(root.get("rating"), maxRating);
        };
    }

    public static Specification<Review> commentContains(String comment) {
        return (root, query, cb) ->
                (comment == null || comment.isBlank()) ? null
                        : cb.like(cb.lower(root.get("comment")), "%" + comment.toLowerCase() + "%");
    }

    public static Specification<Review> hasBookId(Integer bookId) {
        return (root, query, cb) ->
                bookId == null ? null : cb.equal(root.get("book").get("id"), bookId);
    }

    public static Specification<Review> hasAuthorId(Integer authorId) {
        return (root, query, cb) ->
                authorId == null ? null : cb.equal(root.get("book").get("author").get("id"), authorId);
    }

    public static Specification<Review> hasComment(Boolean hasComment) {
        return (root, query, cb) -> {
            if (hasComment == null) return null;

            return hasComment
                    ? cb.isNotNull(root.get("comment"))
                    : cb.isNull(root.get("comment"));
        };
    }
}
