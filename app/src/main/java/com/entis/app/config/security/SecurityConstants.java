package com.entis.app.config.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {

    public static final String AUTH_TOKEN_PREFIX = "Bearer ";

    public static final String AUTHORITIES_CLAIM = "authorities";
}
