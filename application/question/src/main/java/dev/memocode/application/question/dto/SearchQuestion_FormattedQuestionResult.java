package dev.memocode.application.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuestion_FormattedQuestionResult {
    private UUID id;
    private String title;
    private String content;
    private Set<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
    private SearchQuestion_UserResult user;
}
