package dev.memocode.adapter.adapter_batch_memo.out.dto;

import dev.memocode.adapter.adapter_batch_core.MeilisearchUser;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class MeilisearchMemo {
    private UUID id;
    private String title;
    private String content;
    private String summary;
    private String thumbnailUrl;
    private UUID userId;
    private String username;
    private MeilisearchUser user;
    private Boolean visibility;
    private Boolean bookmarked;
    private Boolean security;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Boolean deleted;
    private Set<String> tags;
}
