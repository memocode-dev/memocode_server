package dev.memocode.adapter.question.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeilisearchSearchQuestion_QuestionResult {
    private UUID id;
    private String title;
    private String content;
    private MeilisearchSearchQuestion_QuestionResult _formatted;
    private MeilisearchSearchQuestion_UserResult user;
    private Set<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Boolean deleted;
}
