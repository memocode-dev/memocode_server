package dev.memocode.application.core;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private int page; // 현재 페이지 번호
    private int pageSize; // 페이지 당 항목 수
    private long totalCount; // 전체 항목 수
    private List<T> content; // 페이지에 담긴 실제 내용
    private boolean first;
    private boolean last;
}
