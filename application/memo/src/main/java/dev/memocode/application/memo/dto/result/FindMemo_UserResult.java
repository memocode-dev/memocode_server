package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FindMemo_UserResult {
    private UUID id;
    private String username;
}
