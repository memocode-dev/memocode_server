package dev.memocode.memo_server.in.api.security.config;

import dev.memocode.memo_server.in.api.security.handler.CustomAccessDeniedHandler;
import dev.memocode.memo_server.in.api.security.handler.CustomAuthenticationEntryPoint;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.util.WebUtils;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final static String ACCESS_TOKEN_COOKIE_NAME = "auth.access_token";

    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

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
                        .requestMatchers(GET, "/memos/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/memos/**").access(hasScope("write:memo"))
                        .requestMatchers("/posts/**").permitAll()
                        .requestMatchers("/comments/**").access(hasScope("write:memo")) // 추후 권한 수정
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(o -> o
                        .jwt(Customizer.withDefaults())
                        .bearerTokenResolver(this::tokenExtractor)
                );

        return http.build();
    }

    /**
     * 액세스토큰을 헤더와 쿠키에서 모두 추출하는 함수
     */
    public String tokenExtractor(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null)
            return header.replace("Bearer ", "");
        Cookie cookie = WebUtils.getCookie(request, ACCESS_TOKEN_COOKIE_NAME);
        if (cookie != null)
            return cookie.getValue();
        return null;
    }
}
