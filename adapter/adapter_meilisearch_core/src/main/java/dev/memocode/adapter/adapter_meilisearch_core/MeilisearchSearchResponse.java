package dev.memocode.adapter.adapter_meilisearch_core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MeilisearchSearchResponse<T> {
    @JsonProperty("totalHits")
    private int totalHits;
    @JsonProperty("hitsPerPage")
    private int hitsPerPage;
    @JsonProperty("page")
    private int page;
    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("hits")
    List<T> content;
}
