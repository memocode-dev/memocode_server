package dev.memocode.memo_server.domain.memo.mapper;

import dev.memocode.memo_server.domain.memo.dto.request.PostDetailRequestDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostDtoMapper {

    public PostDetailRequestDTO findPost(UUID memoId, UUID memoVersionId) {
        return PostDetailRequestDTO.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .build();
    }
}
