package dev.memocode.adapter.question.out;

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
    private UUID id;
    private String username;
    private Boolean enabled;
}
