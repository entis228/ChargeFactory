package com.entis.app.entity.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetUserPasswordRequest(

    @NotBlank(message = "password must not be blank")
    @Size(min = 5, message = "password's length must be at least 5") String newPassword
) {

}
