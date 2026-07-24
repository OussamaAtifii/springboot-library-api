package com.atifi.library.auth.exception;

import com.atifi.library.common.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String username) {
        super(username + "not found");
    }
}
