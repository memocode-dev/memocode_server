package dev.memocode.adapter.question.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeilisearchSearchQuestion_UserResult {
    @JsonProperty(value = "id")
    private UUID id;
    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "enabled")
    private Boolean enabled;
}
