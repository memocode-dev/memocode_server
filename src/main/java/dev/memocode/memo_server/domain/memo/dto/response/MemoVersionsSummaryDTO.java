package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
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
public class MemoVersionsSummaryDTO {

    private UUID id;
    private Integer version;
    private String title;
    private Instant createdAt;

    public static MemoVersionsSummaryDTO from(MemoVersion memoVersion){
        return MemoVersionsSummaryDTO.builder()
                .id(memoVersion.getId())
                .version(memoVersion.getVersion())
                .title(memoVersion.getTitle())
                .createdAt(memoVersion.getCreatedAt())
                .build();
    }
}
