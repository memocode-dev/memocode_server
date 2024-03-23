package dev.memocode.memo_server.domain.memo.mapper;

import dev.memocode.memo_server.domain.author.dto.AuthorDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostAuthorDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostsDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    public PostDetailDTO entity_to_postDetailDTO(Memo memo) {
        return PostDetailDTO.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .createdAt(memo.getCreatedAt())
                .author(AuthorDTO.builder()
                        .authorId(memo.getAuthor().getId())
                        .username(memo.getAuthor().getUsername())
                        .nickname(memo.getAuthor().getNickname())
                        .build())
                .build();
    }

    public PostsDTO entity_to_postsDTO(Memo post) {
        return PostsDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .author(AuthorDTO.builder()
                        .authorId(post.getAuthor().getId())
                        .username(post.getAuthor().getUsername())
                        .nickname(post.getAuthor().getNickname())
                        .build())
                .build();
    }

    public Page<PostsDTO> entity_to_postsDTO(Page<Memo> posts) {
        List<PostsDTO> list = posts.stream()
                .map(this::entity_to_postsDTO)
                .toList();

        return PageableExecutionUtils.getPage(list, posts.getPageable(), posts::getTotalElements);
    }

    public Page<PostAuthorDTO> entity_to_postAuthorDto(Page<Memo> posts) {
        List<PostAuthorDTO> list = posts.stream()
                .map(PostAuthorDTO::from)
                .toList();

        return PageableExecutionUtils.getPage(list, posts.getPageable(), posts::getTotalElements);
    }
}
