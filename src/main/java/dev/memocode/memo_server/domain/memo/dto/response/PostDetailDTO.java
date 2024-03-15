package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.author.dto.AuthorDTO;
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
    private AuthorDTO author;
}
