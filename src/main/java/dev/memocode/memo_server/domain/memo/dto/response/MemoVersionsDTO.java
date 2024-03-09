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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoVersionsDTO {
    private int totalPage;
    private int currentPage;
    private boolean isLast;
    private List<MemoVersionTitleDTO> memoVersionTitleDTOS;

    public static MemoVersionsDTO from(Page<Memo> memos) {


        return MemoVersionsDTO.builder()
                .totalPage(memos.getTotalPages())
                .currentPage(memos.getNumber())
                .isLast(memos.isLast())
                .memoVersionTitleDTOS(null)
                .build();
    }
}
