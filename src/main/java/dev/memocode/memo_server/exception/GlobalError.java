package dev.memocode.memo_server.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GlobalError {

    private GlobalErrorCode code;

    private String message;

    private String logMessage;

    public static GlobalError of(GlobalErrorCode code) {
        return GlobalError.builder()
                .code(code)
                .message(code.getMessage())
                .logMessage(code.getMessage())
                .build();
    }

    public static GlobalError of(GlobalErrorCode code, String logMessage) {
        return GlobalError.builder()
                .code(code)
                .message(code.getMessage())
                .logMessage(logMessage)
                .build();
    }
}
