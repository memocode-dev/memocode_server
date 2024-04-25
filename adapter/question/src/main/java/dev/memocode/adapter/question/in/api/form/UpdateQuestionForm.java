package dev.memocode.adapter.question.in.api.form;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UpdateQuestionForm {
    private String title;
    private String content;

    @Builder.Default
    private Set<String> tags = new HashSet<>();
}
