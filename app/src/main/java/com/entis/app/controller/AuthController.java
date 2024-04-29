package com.entis.app.controller;

import com.entis.app.Routes;
import com.entis.app.entity.auth.AuthUserDetails;
import com.entis.app.entity.auth.request.RefreshTokenRequest;
import com.entis.app.entity.auth.request.SignInRequest;
import com.entis.app.entity.auth.response.AccessTokenResponse;
import com.entis.app.exception.auth.InvalidRefreshTokenException;
import com.entis.app.exception.auth.TokenHttpExceptions;
import com.entis.app.service.auth.AuthOperations;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(Routes.TOKEN)
public class AuthController {

    private final AuthOperations authOperations;

    public AuthController(AuthOperations authOperations) {
        this.authOperations = authOperations;
    }

    /*
     * JWTAuthenticationFilter sets the principle (user-details from UserService) using auth manager
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(schema = @Schema(implementation = SignInRequest.class)))
    public AccessTokenResponse login(@AuthenticationPrincipal AuthUserDetails userDetails) {
        return authOperations.getToken(userDetails);
    }

    @PostMapping(
        value = "/refresh",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AccessTokenResponse refresh(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            return authOperations.refreshToken(request.refreshToken());
        } catch (InvalidRefreshTokenException e) {
            throw TokenHttpExceptions.invalidRefreshToken(e);
        }
    }

    @PostMapping(value = "/invalidate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void invalidate(@RequestBody @Valid RefreshTokenRequest request,
                           @AuthenticationPrincipal String email) {
        try {
            authOperations.invalidateToken(request.refreshToken(), email);
        } catch (InvalidRefreshTokenException e) {
            throw TokenHttpExceptions.invalidRefreshToken(e);
        }
    }

}
