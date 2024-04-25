package dev.memocode.application.memo.dto.reque;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchMemoRequest {
    @NotNull
    private String keyword;
    private int page;
    private int pageSize;
}
