package com.entis.app.service.auth;

import com.entis.app.entity.auth.AuthUserDetails;
import com.entis.app.entity.auth.response.AccessTokenResponse;
import com.entis.app.exception.auth.InvalidRefreshTokenException;

public interface AuthOperations {

    AccessTokenResponse getToken(AuthUserDetails userDetails);

    AccessTokenResponse refreshToken(String token) throws InvalidRefreshTokenException;

    void invalidateToken(String token, String email) throws InvalidRefreshTokenException;
}
