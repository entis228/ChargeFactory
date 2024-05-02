package com.entis.app.util.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static void setAuthentication(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /*
    stolen from org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry
    because of the package-private access modifier
     */
    public static RequestMatcher[] antMatchersAsArray(HttpMethod httpMethod,
                                                      String... antPatterns) {
        String method = (httpMethod != null) ? httpMethod.toString() : null;
        RequestMatcher[] matchers = new RequestMatcher[antPatterns.length];
        for (int index = 0; index < antPatterns.length; index++) {
            matchers[index] = new AntPathRequestMatcher(antPatterns[index], method);
        }
        return matchers;
    }
}
