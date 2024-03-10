package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoVersionsDTO {
    private List<MemoVersionTitleDTO> memoVersions;

    public static Page<MemoVersionsDTO> from(Page<MemoVersion> memoVersions) {
        List<MemoVersionTitleDTO> memoVersionTitleDTOS = memoVersions.stream()
                .map(MemoVersionTitleDTO::from)
                .toList();

        return new PageImpl<>(Collections.singletonList(
                MemoVersionsDTO.builder()
                        .memoVersions(memoVersionTitleDTOS)
                        .build()), memoVersions.getPageable(), memoVersions.getTotalElements());
    }
}
