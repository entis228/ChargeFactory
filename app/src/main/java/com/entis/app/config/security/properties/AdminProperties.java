package com.entis.app.config.security.properties;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AdminProperties {

    @Email(message = "email must be a valid email string")
    @NotNull(message = "email must not be null")
    private String email;

    @NotEmpty(message = "password must not be empty")
    @Size(min = 5, message = "password's length must be at least 5")
    private char[] password;
}
