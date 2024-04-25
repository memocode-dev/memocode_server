package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class FindAllMyMemo_MemoResult {
    private UUID id;
    private String title;
    private Boolean visibility;
    private Boolean bookmarked;
    private Boolean security;
    private Instant createdAt;
    private Instant updatedAt;
}
