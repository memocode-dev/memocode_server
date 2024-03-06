package dev.memocode.memo_server.dto.response;

import dev.memocode.memo_server.domain.external.user.entity.Author;
import dev.memocode.memo_server.domain.memo.entity.Memo;
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
    private AuthorDTO authorDTO;

    public static MemoDetailDTO from(Memo memo, Author author) {
        return MemoDetailDTO.builder()
                .title(memo.getTitle())
                .content(memo.getContent())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .authorDTO(AuthorDTO.builder()
                        .username(author.getUsername())
                        .nickname(author.getNickname())
                        .accountId(author.getAccountId())
                        .build())
                .build();
    }
}