package dev.memocode.application.memo.dto.result;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class FindAllMemoComment_MemoCommentResult {
    private UUID id;
    private String content;
    private FindAllMemoComment_UserResult user;
    @JsonBackReference
    private List<FindAllMemoComment_MemoCommentResult> childMemoComments;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;
}
