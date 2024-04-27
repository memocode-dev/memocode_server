package dev.memocode.adapter.memo.out.meilisearch.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MeilisearchSearchMemo_UserResult {
    private UUID id;
    private String username;
    private Boolean enabled;
}
