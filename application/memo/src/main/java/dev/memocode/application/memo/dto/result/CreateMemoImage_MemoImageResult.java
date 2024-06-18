package dev.memocode.application.memo.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemoImage_MemoImageResult {
    private UUID memoImageId;
    private String uploadURL;
    private String extension;
}
