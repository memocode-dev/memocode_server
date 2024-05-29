package dev.memocode.adapter.memo.out.meilisearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import dev.memocode.adapter.adapter_meilisearch_core.MeilisearchSearchResponse;
import dev.memocode.adapter.memo.out.meilisearch.dto.MeilisearchSearchMemo_MemoResult;
import dev.memocode.adapter.memo.out.meilisearch.dto.MeilisearchSearchMemo_UserResult;
import dev.memocode.application.memo.repository.SearchMemoRepository;
import dev.memocode.domain.core.InternalServerException;
import dev.memocode.domain.core.ValidationException;
import dev.memocode.domain.memo.ImmutableMemo;
import dev.memocode.domain.user.ImmutableUser;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.memocode.adapter.adapter_meilisearch_core.AdapterMeilisearchErrorCode.*;

@Repository
@RequiredArgsConstructor
public class MeilisearchSearchMemoRepository implements SearchMemoRepository {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    private final Client client;

    @Value("${custom.meilisearch.index.memos.name}")
    private String meilisearchIndexMemos;

    private final static String[] attributesToRetrieve =
            {"id", "title", "content", "summary", "user", "visibility", "createdAt", "updatedAt", "deletedAt", "deleted", "tags"};
    private final static String[] attributesToHighlight = {"title", "content", "summary", "tags"};
    private final static String[] attributesToCrop = {"content"};
    private final static String[] sort = new String[] {"updatedAt:desc"};
    private final static int cropLength = 50;

    @Override
    public Page<ImmutableMemo> searchMyMemo(User user, String keyword, int page, int pageSize) {
        try {
            SearchRequest request = createSearchMyMemoRequest(user, keyword, page, pageSize);

            Index index = client.getIndex(meilisearchIndexMemos);

            String rawJson = index.rawSearch(request);

            TypeReference<MeilisearchSearchResponse<MeilisearchSearchMemo_MemoResult>> typeRef =
                    new TypeReference<>() {};
            return toEntity(objectMapper.readValue(rawJson, typeRef));
        } catch (JsonProcessingException e) {
            throw new InternalServerException(MEILISEARCH_PARSING_ERROR, e);
        } catch (Exception e) {
            throw new InternalServerException(MEILISEARCH_SEARCH_ERROR, e);
        }
    }

    @Override
    public Page<ImmutableMemo> searchMemoByKeyword(String keyword, int page, int pageSize) {
        try {
            SearchRequest request = createSearchByKeywordMemoRequest(keyword, page, pageSize);

            Index index = client.getIndex(meilisearchIndexMemos);
            String rawJson = index.rawSearch(request);

            TypeReference<MeilisearchSearchResponse<MeilisearchSearchMemo_MemoResult>> typeRef =
                    new TypeReference<>() {};
            return toEntity(objectMapper.readValue(rawJson, typeRef));
        } catch (JsonProcessingException e) {
            throw new InternalServerException(MEILISEARCH_PARSING_ERROR, e);
        } catch (Exception e) {
            throw new InternalServerException(MEILISEARCH_SEARCH_ERROR, e);
        }
    }

    @Override
    public Page<ImmutableMemo> searchMemoByUsername(String username, int page, int pageSize) {
        try {
            SearchRequest request = createSearchByUsernameMemoRequest(username, page, pageSize);

            Index index = client.getIndex(meilisearchIndexMemos);
            String rawJson = index.rawSearch(request);

            TypeReference<MeilisearchSearchResponse<MeilisearchSearchMemo_MemoResult>> typeRef =
                    new TypeReference<>() {};
            return toEntity(objectMapper.readValue(rawJson, typeRef));
        } catch (JsonProcessingException e) {
            throw new InternalServerException(MEILISEARCH_PARSING_ERROR, e);
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
                        "deleted = false",
                        "userId = %s".formatted(user.getId())
                })
                .setSort(sort)
                .setAttributesToRetrieve(attributesToRetrieve)
                .setAttributesToHighlight(attributesToHighlight)
                .setAttributesToCrop(attributesToCrop)
                .setCropLength(cropLength)
                .setPage(page + 1)
                .setHitsPerPage(pageSize);
    }

    private SearchRequest createSearchByKeywordMemoRequest(String keyword, int page, int pageSize) {
        if (page < 0) {
            throw new ValidationException(MEILISEARCH_INVALID_PAGE_NUMBER);
        }

        return new SearchRequest(keyword)
                .setFilter(new String[]{
                        "deleted = false",
                        "visibility = true",
                })
                .setSort(sort)
                .setAttributesToRetrieve(attributesToRetrieve)
                .setAttributesToHighlight(attributesToHighlight)
                .setAttributesToCrop(attributesToCrop)
                .setCropLength(cropLength)
                .setPage(page + 1)
                .setHitsPerPage(pageSize);
    }

    private SearchRequest createSearchByUsernameMemoRequest(String username, int page, int pageSize) {
        if (page < 0) {
            throw new ValidationException(MEILISEARCH_INVALID_PAGE_NUMBER);
        }

        return new SearchRequest("")
                .setFilter(new String[]{
                        "deleted = false",
                        "username = %s".formatted(username)
                })
                .setSort(sort)
                .setAttributesToRetrieve(attributesToRetrieve)
                .setAttributesToHighlight(attributesToHighlight)
                .setAttributesToCrop(attributesToCrop)
                .setCropLength(cropLength)
                .setPage(page + 1)
                .setHitsPerPage(pageSize);
    }

    private Page<ImmutableMemo> toEntity(MeilisearchSearchResponse<MeilisearchSearchMemo_MemoResult> meilisearchSearchResponse) {
        List<ImmutableMemo> content = meilisearchSearchResponse.getContent().stream()
                .map(result -> {
                    MeilisearchSearchMemo_MemoResult formatted = result.get_formatted();
                    MeilisearchSearchMemo_UserResult user = formatted.getUser();
                    ImmutableMemo formattedMemo = ImmutableMemo.builder()
                            .id(formatted.getId())
                            .title(formatted.getTitle())
                            .summary(formatted.getSummary())
                            .content(formatted.getContent())
                            .user(ImmutableUser.builder()
                                    .id(user.getId())
                                    .username(user.getUsername())
                                    .enabled(user.getEnabled())
                                    .build())
                            .tags(formatted.getTags())
                            .visibility(formatted.isVisibility())
                            .createdAt(formatted.getCreatedAt())
                            .updatedAt(formatted.getUpdatedAt())
                            .deleted(formatted.getDeleted())
                            .deletedAt(formatted.getDeletedAt())
                            .build();

                    return ImmutableMemo.builder()
                            .id(result.getId())
                            .title(result.getTitle())
                            .summary(result.getSummary())
                            .content(result.getContent())
                            .user(ImmutableUser.builder()
                                    .id(result.getUser().getId())
                                    .username(result.getUser().getUsername())
                                    .enabled(result.getUser().getEnabled())
                                    .build())
                            .tags(result.getTags())
                            .visibility(result.isVisibility())
                            .createdAt(result.getCreatedAt())
                            .updatedAt(result.getUpdatedAt())
                            .deleted(result.getDeleted())
                            .deletedAt(result.getDeletedAt())
                            .formattedMemo(formattedMemo)
                            .build();
                })
                .toList();

        Pageable pageable = PageRequest.of(
                meilisearchSearchResponse.getPage() - 1, meilisearchSearchResponse.getHitsPerPage());
        return PageableExecutionUtils.getPage(content, pageable, meilisearchSearchResponse::getTotalHits);
    }
}
