package com.atifi.library.author.repository;

import com.atifi.library.author.model.Author;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecifications {
    public static Specification<Author> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Author> hasStatus(String country) {
        return (root, query, cb) ->
                country == null ? null : cb.equal(root.get("country"), country);
    }
}
