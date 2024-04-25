package dev.memocode.application.question.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class SearchQuestion_QuestionResult {
    private UUID id;
    private String title;
    private String content;
    private Set<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
    private SearchQuestion_UserResult user;
}
