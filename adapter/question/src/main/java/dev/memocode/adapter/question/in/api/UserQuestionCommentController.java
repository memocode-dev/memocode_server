package dev.memocode.adapter.question.in.api;

import dev.memocode.adapter.question.in.api.spec.UserQuestionCommentApi;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.question.dto.FindAllQuestionComment_QuestionCommentResult;
import dev.memocode.application.question.dto.FindQuestionCommentByUsernameRequest;
import dev.memocode.application.question.usecase.QuestionCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserQuestionCommentController implements UserQuestionCommentApi {

    private final QuestionCommentUseCase questionCommentUseCase;

//    @GetMapping("/{username}/questionComments")
//    public ResponseEntity<PageResponse<FindAllQuestionComment_QuestionCommentResult>> findAllQuestionCommentByUsername(
//            @PathVariable String username,
//            @PageableDefault(size = 10) Pageable pageable) {
//
//        FindQuestionCommentByUsernameRequest request = FindQuestionCommentByUsernameRequest.builder()
//                .username(username)
//                .pageable(pageable)
//                .build();
//
//        PageResponse<FindAllQuestionComment_QuestionCommentResult> body = questionCommentUseCase.findAllQuestionCommentByUsername(request);
//        return ResponseEntity.ok(body);
//    }
}
