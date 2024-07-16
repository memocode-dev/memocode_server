package dev.memocode.infrastructure.memocode_server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomBearerTokenResolver customBearerTokenResolver;

    private final static String SCOPE_WRITE_MEMO = "write:memo";
    private final static String SCOPE_WRITE_QUESTION = "write:question";

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(a -> a
                        // user
                        .requestMatchers(GET, "/users/**").permitAll()
                        // memo
                        .requestMatchers(GET, "/memos").permitAll()
                        .requestMatchers(GET, "/memos/*").permitAll()
                        .requestMatchers(GET, "/memos/*/comments").permitAll()
                        .requestMatchers(GET, "/memos/*/images/*").permitAll()
                        .requestMatchers( "/memos/**").access(hasScope(SCOPE_WRITE_MEMO))
                        .requestMatchers("/users/memos/**").access(hasScope(SCOPE_WRITE_MEMO))

                        // question
                        .requestMatchers(GET, "/questions").permitAll()
                        .requestMatchers(GET, "/questions/*").permitAll()
                        .requestMatchers(GET, "/questions/*/comments").permitAll()
                        .requestMatchers( "/questions/**").access(hasScope(SCOPE_WRITE_QUESTION))

                        // cookie
                        .requestMatchers(POST, "/cookies").authenticated()
                        .requestMatchers(DELETE, "/cookies").authenticated()

                        // swagger
                        .requestMatchers(GET, "/memocode/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults())
                        .bearerTokenResolver(customBearerTokenResolver)
                );

        return http.build();
    }
}
