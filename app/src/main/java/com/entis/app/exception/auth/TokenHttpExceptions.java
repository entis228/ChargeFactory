package com.entis.app.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TokenHttpExceptions {

    public static ResponseStatusException invalidRefreshToken(InvalidRefreshTokenException cause) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Refresh token is invalid! It may have been rotated, invalidated or expired naturally", cause);
    }

}
