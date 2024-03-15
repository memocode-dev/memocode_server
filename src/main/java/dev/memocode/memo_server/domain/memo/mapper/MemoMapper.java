package dev.memocode.memo_server.domain.memo.mapper;

import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoSummaryDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemoMapper {

    public MemoDetailDTO entity_to_memoDetailDTO(Memo memo) {
        return MemoDetailDTO.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .visibility(memo.getVisibility())
                .security(memo.getSecurity())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }

    public MemosDTO entity_to_memosDTO(List<Memo> memos) {
        List<MemoSummaryDTO> memoDTOs = memos.stream()
                .map(this::entity_to_memoSummaryDTO)
                .collect(Collectors.toList());

        return MemosDTO.builder()
                .data(memoDTOs)
                .build();
    }

    public MemoSummaryDTO entity_to_memoSummaryDTO(Memo memo) {
        return MemoSummaryDTO.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .sequence(memo.getSequence())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }
}
