package dev.memocode.domain.core;

public interface ErrorDetail {
    String getErrorCode();
    String getDefaultMessage();
    ErrorCodeLogLevel getLogLevel();
}
