package dev.memocode.adapter.question.in.api;

import dev.memocode.adapter.question.in.api.form.CreateQuestionCommentForm;
import dev.memocode.adapter.question.in.api.form.UpdateQuestionCommentForm;
import dev.memocode.adapter.question.in.api.spec.QuestionCommentApi;
import dev.memocode.application.question.dto.*;
import dev.memocode.application.question.usecase.QuestionCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions/{questionId}/comments")
public class QuestionCommentController implements QuestionCommentApi {

    private final QuestionCommentUseCase questionCommentUseCase;

    @PostMapping
    public ResponseEntity<String> createQuestionComment(
            @PathVariable UUID questionId, @RequestBody CreateQuestionCommentForm form,
            @AuthenticationPrincipal Jwt jwt) {

        CreateQuestionCommentRequest request = CreateQuestionCommentRequest.builder()
                .questionId(questionId)
                .content(form.getContent())
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        UUID questionCommentId = questionCommentUseCase.createQuestionComment(request);
        return ResponseEntity.created(URI.create(questionCommentId.toString())).body(questionCommentId.toString());
    }

    @PatchMapping("/{questionCommentId}")
    public ResponseEntity<Void> updateQuestionComment(@PathVariable UUID questionId, @PathVariable UUID questionCommentId,
                                                      @RequestBody UpdateQuestionCommentForm form,
                                                      @AuthenticationPrincipal Jwt jwt) {
        UpdateQuestionCommentRequest request = UpdateQuestionCommentRequest.builder()
                .questionId(questionId)
                .questionCommentId(questionCommentId)
                .content(form.getContent())
                .userId(UUID.fromString(jwt.getSubject()))
                .build();
        questionCommentUseCase.updateQuestionComment(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{questionCommentId}")
    public ResponseEntity<Void> deleteQuestionComment(@PathVariable UUID questionId,
                                                      @PathVariable UUID questionCommentId,
                                                      @AuthenticationPrincipal Jwt jwt) {
        DeleteQuestionCommentRequest request = DeleteQuestionCommentRequest.builder()
                .questionId(questionId)
                .questionCommentId(questionCommentId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();
        questionCommentUseCase.deleteQuestionComment(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{questionCommentId}/childComments")
    public ResponseEntity<String> createChildQuestionComment(@PathVariable UUID questionId,
                                                             @PathVariable UUID questionCommentId,
                                                             @RequestBody CreateQuestionCommentForm form,
                                                             @AuthenticationPrincipal Jwt jwt) {
        CreateChildQuestionCommentRequest request = CreateChildQuestionCommentRequest.builder()
                .questionId(questionId)
                .questionCommentId(questionCommentId)
                .userId(UUID.fromString(jwt.getSubject()))
                .content(form.getContent())
                .build();

        UUID childQuestionCommentId = questionCommentUseCase.createChildQuestionComment(request);
        return ResponseEntity.created(URI.create(childQuestionCommentId.toString())).body(childQuestionCommentId.toString());
    }

    @GetMapping
    public ResponseEntity<List<FindAllQuestionComment_QuestionCommentResult>> findAllQuestionComment(@PathVariable UUID questionId) {
        List<FindAllQuestionComment_QuestionCommentResult> body = questionCommentUseCase.findAllQuestionComment(questionId);
        return ResponseEntity.ok(body);
    }
}
