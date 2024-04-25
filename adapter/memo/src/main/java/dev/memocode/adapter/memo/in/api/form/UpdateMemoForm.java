package dev.memocode.adapter.memo.in.api.form;

import lombok.Data;

@Data
public class UpdateMemoForm {
    private String title;
    private String content;
    private String summary;
    private Boolean visibility;
    private Boolean security;
    private Boolean bookmarked;
}
