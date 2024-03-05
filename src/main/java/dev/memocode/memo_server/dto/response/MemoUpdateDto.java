package dev.memocode.memo_server.dto.response;

import dev.memocode.memo_server.dto.form.MemoUpdateForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoUpdateDto {

    private MemoUpdateForm memoUpdateForm;
    private Instant updatedAt;
}
