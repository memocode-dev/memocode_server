package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemosDTO {

    private int totalPage;
    private int currentPage;
    private boolean isLast;
    private List<MemoDetailDTO> memoDetailDTOS;

    public static MemosDTO from(Page<Memo> memos) {
        List<MemoDetailDTO> memoDTOs = memos.stream()
                .map(memo -> MemoDetailDTO.of(memo, memo.getAuthor()))
                .collect(Collectors.toList());

        return MemosDTO.builder()
                .totalPage(memos.getTotalPages())
                .currentPage(memos.getNumber())
                .isLast(memos.isLast())
                .memoDetailDTOS(memoDTOs)
                .build();
    }
}
