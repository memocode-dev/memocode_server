package dev.memocode.memo_server.domain.base.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCodeType.CRITICAL;
import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCodeType.INFO;
import static org.springframework.http.HttpStatus.*;

@Getter
public enum GlobalErrorCode {

    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, 500, "서버 에러, 관리자에게 문의하세요", CRITICAL),
    UNEXPECTED_API_RESPONSE(BAD_GATEWAY, 502, "예상치 못한 API 응답입니다.", CRITICAL),

    AUTHOR_NOT_FOUND(NOT_FOUND, 404, "author not found", INFO),

    // 메모 관련 오류
    MEMO_NOT_FOUND(NOT_FOUND, 404, "memo not found", INFO),
    NOT_VALID_MEMO_OWNER(FORBIDDEN, 403, "메모에 접근할 권한이 없습니다.", INFO),
    MEMO_VERSION_NOT_FOUND(NOT_FOUND, 404, "메모 버전에 접근할 수 없습니다.", INFO),
    PROTECT_MEMO_SECURITY_UNMODIFIED(BAD_REQUEST, 400, "한번 보호된 메모는 보호 모도를 풀 수 없습니다.", INFO),
    PROTECT_MEMO_VISIBILITY_UNMODIFIED(BAD_REQUEST, 400, "한번 보호된 메모는 공개를 하실 수 없습니다.", INFO),
    PROTECT_MODE_DISABLED_ONCE_PUBLIC(BAD_REQUEST, 400, "한번이라도 공개된 메모는 보호 모드를 하실 수 없습니다.", INFO),
    MEMO_AND_MEMO_VERSION_NOT_MATCH(BAD_REQUEST, 400, "메모와 연관되지 않은 메모 버전입니다.", INFO),

    // 게시글 관련 오류
    POST_EXISTS_ERROR(CONFLICT, 409, "이미 게시글에 올라간 메모 입니다.", INFO),
    POST_NOT_FOUND(NOT_FOUND, 404, "찾을 수 없거나 접근할 수 없는 게시글 입니다.", INFO),

    // 게시글 댓글 오류
    POST_COMMENT_NOT_FOUND(NOT_FOUND, 404, "찾을 수 없거나 접근할 수 없는 댓글 입니다.", INFO),
    NOT_VALID_POST_COMMENT_OWNER(FORBIDDEN, 403, "댓글에 접근할 권한이 없습니다.", INFO)
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
