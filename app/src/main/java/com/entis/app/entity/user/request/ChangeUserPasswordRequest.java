package com.entis.app.entity.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeUserPasswordRequest(
    @NotNull String oldPassword,
    @NotBlank(message = "password must be not blank")
    @Size(min = 5, message = "password's length must be at least 5") String newPassword
) {

}
