package dev.memocode.infrastructure.memocode_server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.memocode.adapter.core.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static dev.memocode.adapter.core.CoreAdapterErrorCode.PERMISSION_DENIED;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(PERMISSION_DENIED.getErrorCode())
                .message(PERMISSION_DENIED.getDefaultMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
