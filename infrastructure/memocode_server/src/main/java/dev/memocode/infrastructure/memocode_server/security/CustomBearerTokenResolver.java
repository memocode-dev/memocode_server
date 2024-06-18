package dev.memocode.infrastructure.memocode_server.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CustomBearerTokenResolver implements BearerTokenResolver {

    private static final String AUTH_COOKIE_NAME = "MEMOCODE_ACCESS_TOKEN";
    private final BearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        String token = defaultResolver.resolve(request);
        if (token == null) {
            token = getJwtFromCookies(request);
        }
        return token;
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> AUTH_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
