package dev.memocode.memo_server.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoUpdateForm {
    // createForm 겹치는 문제
    private String title;
    private String content;
}
