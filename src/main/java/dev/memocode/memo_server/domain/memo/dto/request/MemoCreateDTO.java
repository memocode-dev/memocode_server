package dev.memocode.memo_server.domain.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoCreateDTO {
    @NotNull(message = "제목이 비어있습니다.")
    private String title;
    @NotNull(message = "내용이 비어있습니다.")
    private String content;
    @NotNull(message = "요약이 비어있습니다.")
    private String summary;
    private UUID authorId;
}
