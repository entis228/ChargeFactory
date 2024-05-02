package com.entis.app.entity.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SaveUserRequest(@Email(message = "incorrect email")
                              @NotNull(message = "email must not be null")
                              String email,

                              @NotBlank(message = "password must not be blank")
                              @Size(min = 5, message = "password's length must be at least 5")
                              String password,

                              @NotBlank(message = "name must not be blank")
                              String name
) {

}
