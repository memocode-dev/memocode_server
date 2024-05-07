package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class SearchMemo_MemoResult {
    private UUID id;
    private String title;
    private String summary;
    private String content;
    private SearchMemo_UserResult user;
    private SearchMemo_FormattedMemoResult formattedMemo;
    private Instant createdAt;
    private Instant updatedAt;
    private Set<String> tags;
}
