package dev.memocode.adapter.question.out;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "id")
    private UUID id;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "content")
    private String content;
    @JsonProperty(value = "_formatted")
    private MeilisearchSearchQuestion_QuestionResult _formatted;
    @JsonProperty(value = "user")
    private MeilisearchSearchQuestion_UserResult user;
    @JsonProperty(value = "tags")
    private Set<String> tags;
    @JsonProperty(value = "createdAt")
    private Instant createdAt;
    @JsonProperty(value = "updatedAt")
    private Instant updatedAt;
    @JsonProperty(value = "deletedAt")
    private Instant deletedAt;
    @JsonProperty(value = "deleted")
    private Boolean deleted;
}
