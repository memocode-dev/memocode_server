package dev.memocode.memo_server.domain.memo.mapper;

import dev.memocode.memo_server.domain.memo.dto.request.PostCreateDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostDtoMapper {
    public PostCreateDTO createPost(UUID memoId, UUID memoVersionId, UUID accountId) {
        return PostCreateDTO.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .accountId(accountId)
                .build();
    }
}
