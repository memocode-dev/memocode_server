package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.external.author.dto.AuthorDTO;
import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.memo.entity.Memo;
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
public class MemoOneDTO {

    private UUID id;
    private String title;
    private Integer sequence;
    private Instant createdAt;
    private Instant updatedAt;

    public static MemoOneDTO from(Memo memo) {
        return MemoOneDTO.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .sequence(memo.getSequence())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }
}
