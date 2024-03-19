package dev.memocode.memo_server.domain.memo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoSummaryDTO {

    private UUID id;
    private String title;
    private Integer sequence;
    private Boolean visibility;
    private Boolean security;
    private Boolean bookmarked;
    private Instant createdAt;
    private Instant updatedAt;
}
