package com.entis.app.config.security;

import com.entis.app.Routes;
import com.entis.app.config.security.filters.CredentialsAuthenticationFilter;
import com.entis.app.config.security.filters.JWTAuthorizationFilter;
import com.entis.app.config.security.filters.RegularAuthenticationConverter;
import com.entis.app.config.security.properties.SecurityProperties;
import com.entis.app.entity.user.request.SaveUserRequest;
import com.entis.app.service.user.impl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final SecurityProperties securityProperties;

    private final UserService userService;

    private final ObjectMapper objectMapper;

    public SecurityConfig(
        SecurityProperties securityProperties,
        UserService userService,
        ObjectMapper objectMapper
    ) {
        this.securityProperties = securityProperties;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        setupDefaultOwners();
    }

    @Bean
    public AuthenticationManager setAuthenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authenticationManager)
        throws Exception {
        http.authorizeHttpRequests(
                requests -> requests
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, Routes.USERS, Routes.TOKEN, Routes.TOKEN + "/refresh")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, Routes.USERS).hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, Routes.USERS + "/email/*").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, Routes.USERS + "/id/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, Routes.USERS + "/id/**").hasRole("ADMIN")
                    .requestMatchers(Routes.STATIONS).hasRole("ADMIN")
                    .requestMatchers(Routes.STATIONS + "/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, Routes.USERS + "/admins").hasRole("OWNER")
                    .requestMatchers(HttpMethod.POST, Routes.USERS + "/owners").hasRole("OWNER")
                    .requestMatchers(HttpMethod.DELETE, Routes.USERS + "/id/*").hasRole("OWNER")
                    .anyRequest().authenticated())
            .addFilter(credentialsAuthenticationFilter(authenticationManager))
            .addFilter(jwtAuthorizationFilter(authenticationManager))
//            .addFilterAfter(authenticationFilter(authenticationManager), JWTAuthorizationFilter.class)
            .exceptionHandling(configurer -> configurer
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS));
        return http.build();
    }

    private CredentialsAuthenticationFilter credentialsAuthenticationFilter(
        AuthenticationManager authenticationManager) throws Exception {
        var filter = new CredentialsAuthenticationFilter(authenticationManager, objectMapper);
        filter.setFilterProcessesUrl(Routes.TOKEN);
        return filter;
    }

    private AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager){
        return new AuthenticationFilter(authenticationManager, new RegularAuthenticationConverter());
    }

    private JWTAuthorizationFilter jwtAuthorizationFilter(
        AuthenticationManager authenticationManager) throws Exception {
        return new JWTAuthorizationFilter(authenticationManager, securityProperties.getJwt());
    }


    private CorsConfigurationSource corsConfigurationSource() {
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    private void setupDefaultOwners() {
        List<SaveUserRequest> requests = securityProperties.getOwners().entrySet().stream()
            .map(entry -> new SaveUserRequest(
                entry.getValue().getEmail(),
                new String(entry.getValue().getPassword()),
                entry.getKey()))
            .peek(admin -> log.info("Default owner found: {} <{}>", admin.name(), admin.email()))
            .collect(Collectors.toList());
        userService.mergeAdmins(requests);
    }

}
