package dev.memocode.adapter.adapter_api_user.in.api;

import dev.memocode.adapter.adapter_api_user.in.api.spec.UserApi;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.reque.SearchMemoByUsernameRequest;
import dev.memocode.application.memo.dto.request.FindMemoCommentByUsernameRequest;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import dev.memocode.application.memo.dto.result.SearchMemo_MemoResult;
import dev.memocode.application.memo.usecase.MemoCommentUseCase;
import dev.memocode.application.memo.usecase.MemoUseCase;
import dev.memocode.application.question.dto.FindAllQuestionComment_QuestionCommentResult;
import dev.memocode.application.question.dto.FindQuestionCommentByUsernameRequest;
import dev.memocode.application.question.dto.SearchQuestionByUsernameRequest;
import dev.memocode.application.question.dto.SearchQuestion_QuestionResult;
import dev.memocode.application.question.usecase.QuestionCommentUseCase;
import dev.memocode.application.question.usecase.QuestionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserApi{

    private final MemoUseCase memoUseCase;
    private final MemoCommentUseCase memoCommentUseCase;
    private final QuestionUseCase questionUseCase;
    private final QuestionCommentUseCase questionCommentUseCase;

    @GetMapping("/{username}/memos")
    public ResponseEntity<PageResponse<SearchMemo_MemoResult>> searchMemoByUsername(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        SearchMemoByUsernameRequest request = SearchMemoByUsernameRequest.builder()
                .username(username)
                .page(page)
                .pageSize(pageSize)
                .build();
        PageResponse<SearchMemo_MemoResult> body = memoUseCase.searchMemoByUsername(request);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{username}/memoComments")
    public ResponseEntity<PageResponse<FindAllMemoComment_MemoCommentResult>> findAllMemoCommentByUsername(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        FindMemoCommentByUsernameRequest request = FindMemoCommentByUsernameRequest.builder()
                .username(username)
                .pageable(PageRequest.of(page, pageSize))
                .build();

        PageResponse<FindAllMemoComment_MemoCommentResult> body = memoCommentUseCase.findAllMemoCommentByUsername(request);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{username}/questions")
    public ResponseEntity<PageResponse<SearchQuestion_QuestionResult>> searchQuestionByUsername(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        SearchQuestionByUsernameRequest request = SearchQuestionByUsernameRequest.builder()
                .username(username)
                .page(page)
                .pageSize(pageSize)
                .build();
        PageResponse<SearchQuestion_QuestionResult> body = questionUseCase.searchQuestionByUsername(request);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{username}/questionComments")
    public ResponseEntity<PageResponse<FindAllQuestionComment_QuestionCommentResult>> findAllQuestionCommentByUsername(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        FindQuestionCommentByUsernameRequest request = FindQuestionCommentByUsernameRequest.builder()
                .username(username)
                .pageable(PageRequest.of(page, pageSize))
                .build();

        PageResponse<FindAllQuestionComment_QuestionCommentResult> body = questionCommentUseCase.findAllQuestionCommentByUsername(request);
        return ResponseEntity.ok(body);
    }
}
