package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FindAllMemoComment_MemoCommentResult {
    private UUID id;
    private String content;
    private FindAllMemoComment_UserResult user;
    private List<FindAllMemoComment_MemoCommentResult> childMemoComments;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;
}
