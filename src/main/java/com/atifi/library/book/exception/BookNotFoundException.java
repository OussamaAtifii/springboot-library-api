package com.atifi.library.book.exception;

import com.atifi.library.common.exception.ResourceNotFoundException;

public class BookNotFoundException extends ResourceNotFoundException {
    public BookNotFoundException(Integer id) {
        super("Book with id " + id + " not found");
    }
}
