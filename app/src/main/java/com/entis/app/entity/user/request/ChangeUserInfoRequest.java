package com.entis.app.entity.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeUserInfoRequest(@Email(message = "incorrect email")
                                    String email,
                                    @NotBlank(message = "name must not be blank")
                                    String name,
                                    String surname,
                                    String phone) {

}
