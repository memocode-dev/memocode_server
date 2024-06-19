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

        // ResponseCookie 생성
        ResponseCookie cookie = ResponseCookie.from("MEMOCODE_ACCESS_TOKEN", token)
                .httpOnly(true) // HttpOnly 설정
                .secure(true) // Secure 설정
                .path("/") // 쿠키의 유효 경로 설정
                .maxAge(5 * 60) // 쿠키의 유효 기간 설정 (예: 5분)
                .sameSite("None") // SameSite=None 설정
                .build();

        // 응답 헤더에 쿠키 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 상태 코드 200 (OK)로 응답
        return ResponseEntity.ok().build();
    }
}
