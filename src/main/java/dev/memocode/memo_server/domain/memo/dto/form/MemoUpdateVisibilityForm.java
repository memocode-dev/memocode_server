package dev.memocode.memo_server.domain.memo.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoUpdateVisibilityForm {
    @Schema(requiredMode = REQUIRED)
    private Boolean visibility;
}
