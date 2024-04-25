package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SearchMyMemo_MemoResult {
    private UUID id;
    private String title;
    private String summary;
    private SearchMyMemo_FormattedMemoResult formattedMemo;
    private Instant createdAt;
    private Instant updatedAt;
}
