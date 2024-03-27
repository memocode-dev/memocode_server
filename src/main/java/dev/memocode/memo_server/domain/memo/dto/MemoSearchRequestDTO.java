package dev.memocode.memo_server.domain.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoSearchRequestDTO {
    private UUID authorId;
    private String keyword;
    private int page;
    private int pageSize;
}
