package dev.memocode.memo_server.exception;

public enum GlobalErrorCodeType {
    INFO,       // 정보적인 메시지나 경고
    WARNING,    // 경고, 심각하지 않은 문제를 알림
    ERROR,      // 일반적인 에러, 개입이 필요한 상황
    CRITICAL    // 매우 심각한 에러, 즉각적인 조치가 필요
}
