package dev.memocode.adapter.question.in.api.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateQuestionCommentForm {
    private String content;
}
