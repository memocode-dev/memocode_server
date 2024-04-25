package dev.memocode.adapter.memo.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import dev.memocode.application.memo.repository.SearchMemoRepository;
import dev.memocode.domain.core.InternalServerException;
import dev.memocode.domain.core.ValidationException;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.memocode.adapter.memo.MemoAdapterErrorCode.*;

@Repository
@RequiredArgsConstructor
public class MeilisearchSearchMemoRepository implements SearchMemoRepository {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    private final Client client;

    @Value("${custom.meilisearch.index.memos}")
    private String meilisearchIndexMemos;

    private final static String[] attributesToRetrieve =
            {"id", "title", "content", "summary", "author_id", "visibility", "created_at", "updated_at"};
    private final static String[] attributesToHighlight = {"title", "content", "summary"};
    private final static String[] attributesToCrop = {"content"};
    private final static int cropLength = 50;

    @Override
    public Page<Memo> searchMyMemo(User user, String keyword, int page, int pageSize) {
        try {
            SearchRequest request = createSearchMyMemoRequest(user, keyword, page, pageSize);

            Index index = client.getIndex(meilisearchIndexMemos);
            String rawJson = index.rawSearch(request);

            return toEntity(objectMapper.readValue(rawJson, MeilisearchSearchMemoResponse.class));
        } catch (JsonProcessingException e) {
            throw new InternalServerException(MEMO_SEARCH_PARSING_ERROR, e);
        } catch (Exception e) {
            throw new InternalServerException(MEILISEARCH_SEARCH_ERROR, e);
        }
    }

    @Override
    public Page<Memo> searchMemo(String keyword, int page, int pageSize) {
        try {
            SearchRequest request = createSearchMemoRequest(keyword, page, pageSize);

            Index index = client.getIndex(meilisearchIndexMemos);
            String rawJson = index.rawSearch(request);

            return toEntity(objectMapper.readValue(rawJson, MeilisearchSearchMemoResponse.class));
        } catch (JsonProcessingException e) {
            throw new InternalServerException(MEMO_SEARCH_PARSING_ERROR, e);
        } catch (Exception e) {
            throw new InternalServerException(MEILISEARCH_SEARCH_ERROR, e);
        }
    }

    private SearchRequest createSearchMyMemoRequest(User user, String keyword, int page, int pageSize) {
        if (page < 0) {
            throw new ValidationException(MEILISEARCH_INVALID_PAGE_NUMBER);
        }

        return new SearchRequest(keyword)
                .setFilter(new String[]{
                        "is_deleted = false",
                        "user_id = %s".formatted(user.getId())
                })
                .setAttributesToRetrieve(attributesToRetrieve)
                .setAttributesToHighlight(attributesToHighlight)
                .setAttributesToCrop(attributesToCrop)
                .setCropLength(cropLength)
                .setPage(page + 1)
                .setHitsPerPage(pageSize);
    }

    private SearchRequest createSearchMemoRequest(String keyword, int page, int pageSize) {
        if (page < 0) {
            throw new ValidationException(MEILISEARCH_INVALID_PAGE_NUMBER);
        }

        return new SearchRequest(keyword)
                .setFilter(new String[]{
                        "is_deleted = false",
                        "visibility = true",
                })
                .setAttributesToRetrieve(attributesToRetrieve)
                .setAttributesToHighlight(attributesToHighlight)
                .setAttributesToCrop(attributesToCrop)
                .setCropLength(cropLength)
                .setPage(page + 1)
                .setHitsPerPage(pageSize);
    }

    private Page<Memo> toEntity(MeilisearchSearchMemoResponse meilisearchSearchMemoResponse) {
        List<Memo> content = meilisearchSearchMemoResponse.getContent().stream()
                .map(result -> {
                    MeilisearchSearchMemo_FormattedMemoResult formatted = result.get_formatted();
                    Memo formattedMemo = Memo.builder()
                            .id(formatted.getId())
                            .title(formatted.getTitle())
                            .summary(formatted.getSummary())
                            .content(formatted.getContent())
                            .user(User.builder()
                                    .id(formatted.getUserId())
                                    .build())
                            .visibility(formatted.isVisibility())
                            .createdAt(formatted.getCreatedAt())
                            .updatedAt(formatted.getUpdatedAt())
                            .build();

                    Memo memo = Memo.builder()
                            .id(result.getId())
                            .title(result.getTitle())
                            .summary(result.getSummary())
                            .content(result.getContent())
                            .user(User.builder()
                                    .id(result.getUserId())
                                    .build())
                            .visibility(result.isVisibility())
                            .createdAt(result.getCreatedAt())
                            .updatedAt(result.getUpdatedAt())
                            .deleted(result.isDeleted())
                            .deletedAt(result.getDeletedAt())
                            .formattedMemo(formattedMemo)
                            .build();

                    return memo;
                })
                .toList();

        Pageable pageable = PageRequest.of(
                meilisearchSearchMemoResponse.getPage() - 1, meilisearchSearchMemoResponse.getHitsPerPage());
        return PageableExecutionUtils.getPage(content, pageable, meilisearchSearchMemoResponse::getTotalHits);
    }
}
