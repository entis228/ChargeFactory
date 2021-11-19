package com.entis.app.entity.user.request;

import javax.validation.constraints.Pattern;

public record TopUpAccountRequest(
        @Pattern(regexp = "\\d+(.\\d+)?", message = "must not contain only numbers or one dot for float point digits")
        String addedSum) {
}
