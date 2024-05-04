package com.entis.app.entity.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeUserStatusRequest(
    @NotNull @NotBlank(message = "user status must be not blank") String status
) {

}
