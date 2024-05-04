package com.entis.app.entity.auth.request;

public record SignInRequest(
    String email,
    String password
) {

}
