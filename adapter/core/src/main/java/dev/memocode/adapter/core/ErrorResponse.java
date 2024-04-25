package dev.memocode.adapter.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String code;
    private String message;
}
