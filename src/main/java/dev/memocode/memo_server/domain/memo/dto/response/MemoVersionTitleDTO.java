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
public class MemoVersionTitleDTO {

    private UUID id;
    private Long version;
    private String title;
    private Instant createdAt;
}
