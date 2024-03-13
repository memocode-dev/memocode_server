package dev.memocode.memo_server.domain.memo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDeleteDTO {

    private UUID memoId;
    private UUID memoVersionId;
    private UUID accountId;
}
