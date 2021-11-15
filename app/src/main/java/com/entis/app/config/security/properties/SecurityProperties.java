package com.entis.app.config.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "file-sharing.security")
public class SecurityProperties {

    @Valid
    @NestedConfigurationProperty
    private JWTProperties jwt;

    private Map<@NotBlank String, @Valid AdminProperties> admins;
}
