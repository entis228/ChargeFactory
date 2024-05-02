package com.entis.app.config.security.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    @Valid
    @NestedConfigurationProperty
    private JWTProperties jwt;

    private Map<@NotBlank String, @Valid OwnerProperties> owners;
}
