package dev.memocode.adapter.question.out;

import com.meilisearch.sdk.SearchRequest;
import dev.memocode.adapter.adapter_meilisearch_core.SearchRequestStrategy;
import dev.memocode.domain.core.ValidationException;

import static dev.memocode.adapter.adapter_meilisearch_core.AdapterMeilisearchErrorCode.MEILISEARCH_INVALID_PAGE_NUMBER;

public class QuestionUsernameSearchStrategy implements SearchRequestStrategy {

    private final static String[] attributesToRetrieve =
            {"id", "title", "content", "tags", "user", "createdAt", "updatedAt", "deleted", "deletedAt"};
    private final static String[] attributesToHighlight = {"title", "content", "tags"};
    private final static String[] attributesToCrop = {"content"};
    private final static String[] sort = new String[] {"updatedAt:desc"};
    private final static int cropLength = 50;


    @Override
    public SearchRequest createSearchRequest(String username, int page, int pageSize) {
        if (page < 0) {
            throw new ValidationException(MEILISEARCH_INVALID_PAGE_NUMBER);
        }

        String[] filters = new String[]{
                "deleted = false",
                "username = %s".formatted(username)
        };

        return new SearchRequest("")
                .setFilter(filters)
                .setSort(sort)
                .setAttributesToRetrieve(attributesToRetrieve)
                .setAttributesToHighlight(attributesToHighlight)
                .setAttributesToCrop(attributesToCrop)
                .setCropLength(cropLength)
                .setPage(page + 1)
                .setHitsPerPage(pageSize);
    }
}
