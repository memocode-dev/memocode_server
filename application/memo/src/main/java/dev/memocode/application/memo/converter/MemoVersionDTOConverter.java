package dev.memocode.application.memo.converter;

import dev.memocode.application.memo.dto.result.FindAllMyMemoVersion_MemoVersionResult;
import dev.memocode.application.memo.dto.result.FindMyMemoVersion_MemoVersionResult;
import dev.memocode.domain.memo.MemoVersion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemoVersionDTOConverter {

    public FindMyMemoVersion_MemoVersionResult toResponse(MemoVersion memoVersion) {
        return FindMyMemoVersion_MemoVersionResult.builder()
                .id(memoVersion.getId())
                .content(memoVersion.getContent())
                .createdAt(memoVersion.getCreatedAt())
                .build();
    }

    public List<FindAllMyMemoVersion_MemoVersionResult> toResponse(List<MemoVersion> memoVersions) {
        return memoVersions.stream().map(this::toResult)
                .toList();
    }

    private FindAllMyMemoVersion_MemoVersionResult toResult(MemoVersion memoVersion) {
        return FindAllMyMemoVersion_MemoVersionResult.builder()
                .id(memoVersion.getId())
                .content(memoVersion.getContent())
                .createdAt(memoVersion.getCreatedAt())
                .build();
    }
}
