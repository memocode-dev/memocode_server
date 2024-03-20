package dev.memocode.memo_server.domain.memocomment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildCommentCreateDTO {

    private UUID memoId;
    private UUID commentId;
    private String content;
    private UUID authorId;
}
