package dev.memocode.application.question.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@Builder
public class FindQuestionCommentByUsernameRequest {
    @NotNull
    private String username;
    private Pageable pageable;
}
