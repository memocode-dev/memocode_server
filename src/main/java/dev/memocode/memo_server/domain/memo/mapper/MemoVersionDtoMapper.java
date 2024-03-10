package dev.memocode.memo_server.domain.memo.mapper;

import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionRequestDetailDTO;
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

    public MemoVersionDeleteDTO deleteMemoVersion(UUID memoId, UUID memoVersionId, UUID accountId) {
        return MemoVersionDeleteDTO.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .accountId(accountId)
                .build();
    }

    public MemoVersionRequestDetailDTO findMemoVersionDetail(UUID memoId, UUID memoVersionId, UUID accountId) {
        return MemoVersionRequestDetailDTO.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .accountId(accountId)
                .build();
    }
}
