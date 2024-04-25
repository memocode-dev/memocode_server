package dev.memocode.adapter.memo.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MeilisearchSearchMemo_MemoResult {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("content")
    private String content;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("visibility")
    private boolean visibility;

    @JsonProperty("_formatted")
    private MeilisearchSearchMemo_FormattedMemoResult _formatted;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonProperty("deleted_at")
    private Instant deletedAt;

    @JsonProperty("deleted")
    private boolean deleted;

}
