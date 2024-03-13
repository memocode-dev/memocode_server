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

    private List<MemoOneDTO> data;

    public static MemosDTO from(List<Memo> memos) {
        List<MemoOneDTO> memoDTOs = memos.stream()
                .map(MemoOneDTO::from)
                .collect(Collectors.toList());

        return MemosDTO.builder()
                .data(memoDTOs)
                .build();
    }
}
