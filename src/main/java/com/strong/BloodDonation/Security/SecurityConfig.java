package com.strong.BloodDonation.Security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${bloodDonation.Cors.Url}")
    private String CORS_URL;

    @Value("${bloodDonation.Cors.Methods}")
    private String CORS_METHODS;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure CORS settings
        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(@SuppressWarnings("null") HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList(CORS_URL));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));/* CORS_METHODS.split(",")) */
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                config.setMaxAge(3600L);
                config.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION));
                return config;
            }
        }));

        // CSRF protection is disabled as it's typically not needed for stateless
        // RESTful APIs
        http.csrf((csrf) -> csrf.disable());

        // Configure authorization for requests
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.POST, "/**").permitAll() // Allow all POST requests
                .anyRequest().authenticated())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
