package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.external.author.dto.AuthorDTO;
import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.memo.entity.Memo;
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
    private AuthorDTO authorDTO;

    public static MemoVersionDetailDTO of(MemoVersion memoVersion, Author author) {
        return MemoVersionDetailDTO.builder()
                .title(memoVersion.getTitle())
                .content(memoVersion.getContent())
                .createdAt(memoVersion.getCreatedAt())
                .updatedAt(memoVersion.getUpdatedAt())
                .authorDTO(AuthorDTO.builder()
                        .username(author.getUsername())
                        .nickname(author.getNickname())
                        .accountId(author.getAccountId())
                        .build())
                .build();
    }
}
