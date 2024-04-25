package dev.memocode.application.question.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchQuestionRequest {
    @NotNull
    private String keyword;
    private int page;
    private int pageSize;
}
