package dev.memocode.memo_server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static dev.memocode.memo_server.exception.GlobalErrorCodeType.CRITICAL;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum GlobalErrorCode {

    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, 500, "서버 에러, 관리자에게 문의하세요", CRITICAL),
    UNEXPECTED_API_RESPONSE(BAD_GATEWAY, 502, "예상치 못한 API 응답입니다.", CRITICAL),
    ;

    private final HttpStatus status;
    private final int code;
    private final String message;
    private final GlobalErrorCodeType type;

    GlobalErrorCode(HttpStatus status, int code, String message, GlobalErrorCodeType type) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.type = type;
    }
}
