package dev.memocode.adapter.memo.in.api;

import dev.memocode.adapter.memo.in.api.spec.UserMemoCommentApi;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.request.FindMemoCommentByUsernameRequest;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import dev.memocode.application.memo.usecase.MemoCommentUseCase;
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
public class UserMemoCommentController implements UserMemoCommentApi {

    private final MemoCommentUseCase memoCommentUseCase;

//    @GetMapping("/{username}/memoComments")
//    public ResponseEntity<PageResponse<FindAllMemoComment_MemoCommentResult>> findAllQuestionCommentByUsername(
//            @PathVariable String username,
//            @PageableDefault(size = 10) Pageable pageable) {
//
//        FindMemoCommentByUsernameRequest request = FindMemoCommentByUsernameRequest.builder()
//                .username(username)
//                .pageable(pageable)
//                .build();
//
//        PageResponse<FindAllMemoComment_MemoCommentResult> body = memoCommentUseCase.findAllMemoCommentByUsername(request);
//        return ResponseEntity.ok(body);
//    }
}
