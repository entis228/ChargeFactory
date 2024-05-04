package com.entis.app.config.security.filters.converter;

import com.entis.app.util.security.SecurityUtils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class JWTAnonymousAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        Authentication authentication = SecurityUtils.getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return authentication;
    }
}
