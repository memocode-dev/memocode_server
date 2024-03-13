package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsDTO {

    private List<PostDetailDTO> posts;

    public static Page<PostsDTO> from(Page<Memo> posts) {
        List<PostDetailDTO> postsDTO = posts.stream()
                .map(PostDetailDTO::from)
                .toList();

        return new PageImpl<>(Collections.singletonList(
                PostsDTO.builder()
                        .posts(postsDTO)
                        .build()), posts.getPageable(), posts.getTotalElements());
    }
}
