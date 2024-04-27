package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateMemoRequest {
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String summary;
    @NotNull
    private UUID userId;
    @NotNull
    private Boolean security;
}
