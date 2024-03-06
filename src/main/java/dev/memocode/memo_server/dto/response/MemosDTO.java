package dev.memocode.memo_server.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemosDTO {

    private int totalPage;
    private int currentPage;
    private boolean isLast;
    private List<MemoDetailDTO> memoDetailDTOS;
}
