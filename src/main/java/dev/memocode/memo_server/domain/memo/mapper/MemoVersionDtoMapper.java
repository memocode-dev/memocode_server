package dev.memocode.memo_server.domain.memo.mapper;

import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemoVersionDtoMapper {


    public MemoVersionCreateDTO createMemoVersion(UUID memoId, UUID accountId) {
        return MemoVersionCreateDTO.builder()
                .memoId(memoId)
                .accountId(accountId)
                .build();
    }
}
