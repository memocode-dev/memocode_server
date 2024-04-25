package dev.memocode.application.question.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class FindQuestion_QuestionResult {
    private UUID id;
    private String title;
    private FindQuestion_UserResult user;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}
