package com.entis.app.entity.user.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record SetUserPasswordRequest(
        @NotNull
        String userId,

        @NotBlank(message = "password must not be blank")
        @Size(min = 5, message = "password's length must be at least 5")
        String newPassword
) {
}
