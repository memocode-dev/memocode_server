package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.external.author.dto.AuthorDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsDTO {

    private UUID id;
    private String title;
    private String content;
    private Instant createdAt;
    private AuthorDTO authorDTO;

    public static PostsDTO from(Memo post) {
        return PostsDTO.builder()
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

    public static Page<PostsDTO> from(Page<Memo> posts) {
        List<PostsDTO> list = posts.stream()
                .map(PostsDTO::from)
                .toList();

        return PageableExecutionUtils.getPage(list, posts.getPageable(), posts::getTotalElements);
    }
}
