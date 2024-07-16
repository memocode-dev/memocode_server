package dev.memocode.adapter.memo.in.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemoForm {
    private String title;
    private String content;
    private String summary;
    private String thumbnailUrl;
    private Boolean visibility;
    private Boolean security;
    private Boolean bookmarked;
    private Set<String> tags;
}
