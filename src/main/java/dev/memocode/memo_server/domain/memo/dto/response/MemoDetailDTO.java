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
public class MemoDetailDTO {
    private UUID id;
    private String title;
    private String content;
    private Boolean visibility;
    private Boolean security;
    private Instant createdAt;
    private Instant updatedAt;
}