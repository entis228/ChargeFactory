package com.entis.app.entity.auth.response;

public record AccessTokenResponse(
    String accessToken,
    String refreshToken,
    long expireIn
) {

}
