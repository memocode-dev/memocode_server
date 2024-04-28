package dev.memocode.adapter.question.out;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MeilisearchSearchQuestion_UserResult {
    private UUID id;
    private String username;
    private Boolean enabled;
}
