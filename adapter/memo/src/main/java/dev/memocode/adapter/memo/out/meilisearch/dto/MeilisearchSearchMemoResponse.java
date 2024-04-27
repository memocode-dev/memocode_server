package dev.memocode.adapter.memo.out.meilisearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MeilisearchSearchMemoResponse {
    @JsonProperty("totalHits")
    private int totalHits;
    @JsonProperty("hitsPerPage")
    private int hitsPerPage;
    @JsonProperty("page")
    private int page;
    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("hits")
    List<MeilisearchSearchMemo_MemoResult> content;
}
