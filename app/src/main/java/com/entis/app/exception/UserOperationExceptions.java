package com.entis.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserOperationExceptions {

    public static ResponseStatusException userWithEmailNotFound(String email) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " not found");
    }

    public static ResponseStatusException userWithIdNotFound(String id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
    }

    public static ResponseStatusException duplicateEmail(String email) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email " + email + " already exists");
    }

    public static ResponseStatusException authorityNotFound(String authorityName) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Authority " + authorityName + " not in database");
    }

    public static ResponseStatusException incorrectPassword(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
