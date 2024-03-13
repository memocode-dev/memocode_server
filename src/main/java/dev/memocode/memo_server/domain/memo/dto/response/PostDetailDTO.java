package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.external.author.dto.AuthorDTO;
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
public class PostDetailDTO {

    private UUID id;
    private String title;
    private String content;
    private Instant createdAt;
    private AuthorDTO authorDTO;

    public static PostDetailDTO from(Memo post) {
        return PostDetailDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .authorDTO(AuthorDTO.builder()
                        .username(post.getAuthor().getUsername())
                        .nickname(post.getAuthor().getNickname())
                        .accountId(post.getAuthor().getAccountId())
                        .build())
                .build();
    }
}
