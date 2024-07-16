package dev.memocode.infrastructure.memocode_server.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cookies")
@Tag(name = "cookie", description = "쿠키 API")
@SecurityRequirement(name = "bearer-key")
public class CookieController {

    @PostMapping
    @Operation(summary = "ACCESS TOKEN을 쿠키로 생성")
    public ResponseEntity<Void> createAccessTokenCookie(@AuthenticationPrincipal Jwt jwt, HttpServletResponse response) {
        String token = jwt.getTokenValue();

        ResponseCookie cookie = ResponseCookie.from("MEMOCODE_ACCESS_TOKEN", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(5 * 60)
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "ACCESS TOKEN 쿠키 삭제")
    public ResponseEntity<Void> deleteAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("MEMOCODE_ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }
}
