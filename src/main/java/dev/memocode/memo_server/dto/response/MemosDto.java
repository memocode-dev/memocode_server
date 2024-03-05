package dev.memocode.memo_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
public class MemosDto {

    private int totalPage;
    private int currentPage;
    private boolean isLast;
    private List<MemoDetailDto> memoDetailDtos;
}
