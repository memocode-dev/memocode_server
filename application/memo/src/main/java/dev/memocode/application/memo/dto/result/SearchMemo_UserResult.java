package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SearchMemo_UserResult {
    private UUID id;
}
