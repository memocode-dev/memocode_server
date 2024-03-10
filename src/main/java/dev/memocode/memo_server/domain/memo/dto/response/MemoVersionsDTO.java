package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoVersionsDTO {
    private int totalPage;
    private int currentPage;
    private boolean isLast;
    private List<MemoVersionTitleDTO> memoVersionTitleDTOS;

    public static MemoVersionsDTO from(Page<MemoVersion> memoVersions) {
        List<MemoVersionTitleDTO> memoVersionTitleDTOS = memoVersions.stream()
                .map(MemoVersionTitleDTO::from)
                .toList();

        return MemoVersionsDTO.builder()
                .totalPage(memoVersions.getTotalPages())
                .currentPage(memoVersions.getNumber())
                .isLast(memoVersions.isLast())
                .memoVersionTitleDTOS(memoVersionTitleDTOS)
                .build();
    }
}
