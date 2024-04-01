package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.author.dto.AuthorDTO;
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
public class PostAuthorDTO {
    private UUID id;
    private String title;
    private String content;
    private Integer commentCounts;
    private Instant cratedAt;
    private Instant updatedAt;
    private AuthorDTO author;

    public static PostAuthorDTO from(Memo post) {
        return PostAuthorDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCounts(post.getComments().size())
                .cratedAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .author(AuthorDTO.builder()
                        .authorId(post.getAuthor().getId())
                        .username(post.getAuthor().getUsername())
                        .nickname(post.getAuthor().getNickname())
                        .build())
                .build();
    }
}
