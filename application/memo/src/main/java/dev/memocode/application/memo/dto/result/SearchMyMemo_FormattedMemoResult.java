package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SearchMyMemo_FormattedMemoResult {
    private UUID id;
    private String title;
    private String summary;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}
