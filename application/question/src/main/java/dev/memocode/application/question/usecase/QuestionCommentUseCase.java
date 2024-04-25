package dev.memocode.application.question.usecase;

import dev.memocode.application.question.dto.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface QuestionCommentUseCase {
    UUID createQuestionComment(@Valid CreateQuestionCommentRequest request);

    void updateQuestionComment(@Valid UpdateQuestionCommentRequest request);

    void deleteQuestionComment(@Valid DeleteQuestionCommentRequest request);
    UUID createChildQuestionComment(@Valid CreateChildQuestionCommentRequest request);

    List<FindAllQuestionComment_QuestionCommentResult> findAllQuestionComment(UUID questionId);
}
