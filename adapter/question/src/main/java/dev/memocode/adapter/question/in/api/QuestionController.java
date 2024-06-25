package dev.memocode.adapter.question.in.api;

import dev.memocode.adapter.question.in.api.form.CreateQuestionForm;
import dev.memocode.adapter.question.in.api.form.UpdateQuestionForm;
import dev.memocode.adapter.question.in.api.spec.QuestionApi;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.question.dto.*;
import dev.memocode.application.question.usecase.QuestionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController implements QuestionApi {

    private final QuestionUseCase questionUseCase;

    @PostMapping
    public ResponseEntity<String> createQuestion(@RequestBody CreateQuestionForm form, @AuthenticationPrincipal Jwt jwt) {

        CreateQuestionRequest request = CreateQuestionRequest.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .tags(form.getTags())
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        UUID questionId = questionUseCase.createQuestion(request);
        return ResponseEntity.created(URI.create(questionId.toString())).body(questionId.toString());
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID questionId, @AuthenticationPrincipal Jwt jwt) {

        DeleteQuestionRequest request = DeleteQuestionRequest.builder()
                .questionId(questionId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        questionUseCase.deleteQuestion(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{questionId}")
    public ResponseEntity<Void> updateQuestion(
            @PathVariable UUID questionId, @RequestBody UpdateQuestionForm form, @AuthenticationPrincipal Jwt jwt) {

        UpdateQuestionRequest request = UpdateQuestionRequest.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .tags(form.getTags())
                .questionId(questionId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        questionUseCase.updateQuestion(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<FindQuestion_QuestionResult> findQuestion(@PathVariable UUID questionId) {
        FindQuestion_QuestionResult body = questionUseCase.findQuestion(questionId);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<PageResponse<SearchQuestion_QuestionResult>> searchQuestionByKeyword(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        SearchQuestionRequest request = SearchQuestionRequest.builder()
                .keyword(keyword)
                .page(page)
                .pageSize(pageSize)
                .build();

        PageResponse<SearchQuestion_QuestionResult> body = questionUseCase.searchQuestionByKeyword(request);
        return ResponseEntity.ok(body);
    }
}
