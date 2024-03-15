package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.external.author.dto.AuthorDTO;
import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import jakarta.persistence.Column;
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

    public static MemoDetailDTO from(Memo memo) {
        return MemoDetailDTO.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .visibility(memo.getVisibility())
                .security(memo.getSecurity())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }
}