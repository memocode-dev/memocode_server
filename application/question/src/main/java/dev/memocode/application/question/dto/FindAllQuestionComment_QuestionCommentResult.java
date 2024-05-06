package dev.memocode.application.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class FindAllQuestionComment_QuestionCommentResult {
    private UUID id;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean deleted;
    private FindAllQuestionComment_UserResult user;
    @ArraySchema(schema = @Schema(implementation = FindAllQuestionComment_QuestionCommentResult.class))
    private List<FindAllQuestionComment_QuestionCommentResult> childQuestionComments;
}
