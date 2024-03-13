package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.external.author.dto.AuthorDTO;
import dev.memocode.memo_server.domain.memo.entity.SelectedMemoVersion;
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
public class PostDetailDTO {

    private UUID id;
    private String title;
    private String content;
    private Instant createdAt;
    private AuthorDTO authorDTO;

    public static PostDetailDTO from(SelectedMemoVersion post) {
        return PostDetailDTO.builder()
                .id(post.getId())
                .title(post.getMemoVersion().getTitle())
                .content(post.getMemoVersion().getContent())
                .createdAt(post.getCreatedAt())
                .authorDTO(AuthorDTO.builder()
                        .username(post.getMemo().getAuthor().getUsername())
                        .nickname(post.getMemo().getAuthor().getNickname())
                        .accountId(post.getMemo().getAuthor().getAccountId())
                        .build())
                .build();
    }
}
