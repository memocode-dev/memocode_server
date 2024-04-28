package dev.memocode.adapter.adapter_batch_question.out.dto;

import dev.memocode.adapter.adapter_batch_core.MeilisearchUser;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class MeilisearchQuestion {
    private UUID id;
    private String title;
    private String content;
    private UUID userId;
    private Set<String> tags;
    private MeilisearchUser user;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Boolean deleted;
}
