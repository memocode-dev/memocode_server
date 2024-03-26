package dev.memocode.memo_server.domain.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoSearchDTO {
    private UUID id;
    private String title;
    private String summary;
}
