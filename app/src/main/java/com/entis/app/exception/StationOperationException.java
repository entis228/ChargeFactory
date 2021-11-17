package com.entis.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StationOperationException {

    public static ResponseStatusException stationWithIdNotFound(String id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Station with id " + id + " not found");
    }
}
