package dev.memocode.application.question.usecase;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.question.dto.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface QuestionUseCase {
    UUID createQuestion(@Valid CreateQuestionRequest request);

    void updateQuestion(@Valid UpdateQuestionRequest request);

    void deleteQuestion(@Valid DeleteQuestionRequest request);

    FindQuestion_QuestionResult findQuestion(UUID questionId);

    PageResponse<SearchQuestion_QuestionResult> searchQuestionByUsername(@Valid FindQuestionRequest request);

    PageResponse<SearchQuestion_QuestionResult> searchQuestionByKeyword(@Valid SearchQuestionRequest request);
}
