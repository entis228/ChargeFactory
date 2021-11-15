package com.entis.app.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum KnownAuthority implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_OWNER;

    @Override
    public String getAuthority() {
        return name();
    }
}
