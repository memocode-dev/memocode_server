package dev.memocode.memo_server.domain.memo.dto.response;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoVersionTitleDTO {

    private UUID id;
    private Integer version;
    private String title;
    private Instant createdAt;

    public static MemoVersionTitleDTO from(MemoVersion memoVersion){
        return MemoVersionTitleDTO.builder()
                .id(memoVersion.getId())
                .version(memoVersion.getVersion())
                .title(memoVersion.getTitle())
                .createdAt(memoVersion.getCreatedAt())
                .build();
    }

    public static Page<MemoVersionTitleDTO> from(Page<MemoVersion> memoVersions){
        List<MemoVersionTitleDTO> list = memoVersions.stream()
                .map(MemoVersionTitleDTO::from)
                .toList();

        return PageableExecutionUtils.getPage(list, memoVersions.getPageable(), memoVersions::getTotalElements);

    }
}
