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
public class CommentDeleteDto {
    private UUID memoId;
    private UUID commentId;
    private UUID authorId;
}
