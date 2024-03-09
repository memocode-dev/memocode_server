package dev.memocode.memo_server.domain.memo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoUpdateDTO {

    private UUID memoId;
    private UUID accountId;
    private String title;
    private String content;
}
