package dev.memocode.adapter.memo.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MeilisearchSearchMemo_FormattedMemoResult {
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

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}
