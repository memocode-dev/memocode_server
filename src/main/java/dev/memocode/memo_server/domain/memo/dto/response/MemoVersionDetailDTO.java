package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoVersionDetailDTO {
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;

    public static MemoVersionDetailDTO of(MemoVersion memoVersion) {
        return MemoVersionDetailDTO.builder()
                .title(memoVersion.getTitle())
                .content(memoVersion.getContent())
                .createdAt(memoVersion.getCreatedAt())
                .updatedAt(memoVersion.getUpdatedAt())
                .build();
    }
}
