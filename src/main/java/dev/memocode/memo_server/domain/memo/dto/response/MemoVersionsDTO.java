package dev.memocode.memo_server.domain.memo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoVersionsDTO {

    private List<MemoVersionsSummaryDTO> data;
}
