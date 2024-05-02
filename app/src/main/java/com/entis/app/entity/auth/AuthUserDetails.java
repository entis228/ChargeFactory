package com.entis.app.entity.auth;

import com.entis.app.entity.user.User;
import com.entis.app.entity.user.UserStatus;

import java.util.EnumSet;
import lombok.Getter;

@Getter
public class AuthUserDetails extends org.springframework.security.core.userdetails.User {

    private final User source;

    public AuthUserDetails(User source) {
        super(source.getEmail(),
                source.getPassword(),
                source.getStatus() == UserStatus.ACTIVE,
                true,
                true,
                true,
                EnumSet.copyOf(source.getAuthorities().keySet())
        );
        this.source = source;
    }

}
