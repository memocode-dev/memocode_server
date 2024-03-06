package dev.memocode.memo_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoDetailDTO {

    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}
