package com.entis.app.entity.auth.request;

import javax.validation.constraints.NotNull;

public record RefreshTokenRequest(@NotNull String refreshToken) {

}
