package dev.memocode.adapter.memo.out.meilisearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeilisearchSearchMemo_MemoResult {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("content")
    private String content;

    @JsonProperty("visibility")
    private boolean visibility;

    @JsonProperty("user")
    private MeilisearchSearchMemo_UserResult user;

    @JsonProperty("_formatted")
    private MeilisearchSearchMemo_MemoResult _formatted;

    @JsonProperty("createdAt")
    private Instant createdAt;

    @JsonProperty("updatedAt")
    private Instant updatedAt;

    @JsonProperty("deletedAt")
    private Instant deletedAt;

    @JsonProperty("deleted")
    private boolean deleted;

}
