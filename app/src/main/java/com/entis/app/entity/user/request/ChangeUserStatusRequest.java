package com.entis.app.entity.user.request;

import com.entis.app.entity.user.UserStatus;

import javax.validation.constraints.NotNull;

public record ChangeUserStatusRequest(@NotNull UserStatus status) {
}
