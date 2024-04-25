package dev.memocode.domain.question;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateQuestionCommentDomainDTO {
    private String content;
}
