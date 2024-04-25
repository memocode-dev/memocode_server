package dev.memocode.domain.question;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateQuestionCommentDomainDTO {
    @NotNull
    private String content;
}
