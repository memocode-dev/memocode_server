package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class CreateMemoRequest {
    private String title;
    private String content;
    private String summary;
    private Boolean security;

    @NotNull
    private UUID userId;

    @NotNull
    private Set<String> tags;
}
