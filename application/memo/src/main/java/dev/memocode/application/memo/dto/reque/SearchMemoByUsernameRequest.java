package dev.memocode.application.memo.dto.reque;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchMemoByUsernameRequest {
    @NotNull
    private String username;
    private int page;
    private int pageSize;
}
