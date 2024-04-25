package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SearchMyMemoRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private String keyword;

    private int page;

    private int pageSize;
}
