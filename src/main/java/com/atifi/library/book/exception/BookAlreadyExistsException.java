package com.atifi.library.book.exception;

import com.atifi.library.common.exception.ResourceAlreadyExistsException;

public class BookAlreadyExistsException extends ResourceAlreadyExistsException {
    public BookAlreadyExistsException(String isbn) {
        super("Book with ISBN " + isbn + " already exists");
    }
}
