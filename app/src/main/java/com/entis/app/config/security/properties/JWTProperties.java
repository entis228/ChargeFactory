package com.entis.app.config.security.properties;

import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

@Getter
@Setter
public class JWTProperties {

    @NotEmpty
    private char[] secret;

    @DurationMax(minutes = 30)
    @DurationMin(minutes = 1)
    private Duration accessExpireIn;

    @DurationMax(days = 7)
    @DurationMin(hours = 12)
    private Duration refreshExpireIn;

}
