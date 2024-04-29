package com.entis.app.entity.user.request;

import com.entis.app.entity.user.UserStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeUserStatusRequest(@NotNull UserStatus status) {

}
