package com.entis.app.entity.auth.request;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(@NotNull String refreshToken) {

}
