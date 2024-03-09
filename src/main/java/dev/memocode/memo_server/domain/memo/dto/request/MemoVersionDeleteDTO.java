package dev.memocode.memo_server.domain.memo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoVersionDeleteDTO {

    private UUID memoId;
    private UUID memoVersionId;
    private UUID accountId;
}
