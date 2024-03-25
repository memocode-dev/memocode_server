package dev.memocode.memo_server.in.api.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoUpdateForm {
    private String title;
    private String content;
    private String summary;
    private Boolean visibility;
    private Boolean security;
    private Boolean bookmarked;
}
