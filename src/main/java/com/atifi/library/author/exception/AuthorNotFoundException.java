package com.atifi.library.author.exception;

import com.atifi.library.common.exception.ResourceNotFoundException;

public class AuthorNotFoundException extends ResourceNotFoundException {
    public AuthorNotFoundException(Integer id) {
        super("Author with id " + id + " not found");
    }
}
