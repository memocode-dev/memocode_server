package dev.memocode.application.question.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindQuestionRequest {
    @NotNull
    private String username;
    private int page;
    private int pageSize;
}
