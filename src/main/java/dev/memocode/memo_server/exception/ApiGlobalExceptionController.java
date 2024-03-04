package dev.memocode.memo_server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiGlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception ex) {

        ErrorResponse response = ErrorResponse.builder()
                .code(500)
                .codeString("INTERNAL_ERROR")
                .message("서버 에러, 관리자에게 문의하세요")
                .build();

        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> globalException(GlobalException ex) {

        GlobalError error = ex.getError();

        ErrorResponse response = ErrorResponse.builder()
                .code(error.getCode().getCode())
                .codeString(error.getCode().name())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(error.getCode().getCode()).body(response);
    }
}
