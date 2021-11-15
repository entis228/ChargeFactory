package com.entis.app.entity.user.response;

import com.entis.app.entity.user.KnownAuthority;
import com.entis.app.entity.user.User;
import com.entis.app.entity.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.EnumSet;
import java.util.Set;

public record UserResponse(String id,
                           String email,
                           String name,
                           UserStatus status,
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                           Set<KnownAuthority> authorities) {

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getEmail(),
                user.getName(),
                user.getStatus(),
                EnumSet.copyOf(user.getAuthorities().keySet()));
    }

    // only the attributes that don't require extra fetching
    public static UserResponse fromUserWithBasicAttributes(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getEmail(),
                user.getName(),
                user.getStatus(),
                null);
    }
}

