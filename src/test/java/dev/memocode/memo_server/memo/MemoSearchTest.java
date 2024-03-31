package dev.memocode.memo_server.memo;

import com.meilisearch.sdk.model.SearchResultPaginated;
import dev.memocode.memo_server.base.BaseTest;
import dev.memocode.memo_server.domain.memo.dto.MemoSearchRequestDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.usecase.MemoUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoSearchTest extends BaseTest {

    @Autowired
    private MemoUseCase memoUseCase;

    @Test
    @DisplayName("title과 content에 포함된 내용만 검색 && author_id를 기준 조회")
    void test1() throws InterruptedException {

        MemoCreateDTO memoCreateDTO = MemoCreateDTO.builder()
                .title("메모코드에 오신 것을 환영합니다.")
                .content("내용")
                .summary("요약")
                .authorId(author.getId())
                .build();

        memoUseCase.createMemo(memoCreateDTO);

        logstash_restart();

        SearchResultPaginated searchResult = searchTest("메모코드", author.getId());

        assertThat(searchResult.getHits()).isNotNull();
    }

    @Test
    @DisplayName("title과 content에 포함된 내용만 검색 && 랜덤한 author_id를 사용하여 자신이 작성하지 않은 메모는 검색되지 않은지 확인")
    void test2() throws InterruptedException {

        MemoCreateDTO memoCreateDTO = MemoCreateDTO.builder()
                .title("메모코드에 오신 것을 환영합니다.")
                .content("내용")
                .summary("요약")
                .authorId(author.getId())
                .build();

        memoUseCase.createMemo(memoCreateDTO);

        logstash_restart();

        SearchResultPaginated searchResult = searchTest("메모코드", UUID.randomUUID());

        assertThat(searchResult.getHits()).isNull();
    }

    private void logstash_restart() {
        logstash.stop();
        logstash.start();
    }

    private SearchResultPaginated searchTest(String keyword, UUID authorId) throws InterruptedException {
        int attempt = 0;

        while (attempt < 100) {
            Thread.sleep(1000);
            attempt++;

            MemoSearchRequestDTO dto = MemoSearchRequestDTO.builder()
                    .page(1)
                    .pageSize(20)
                    .keyword(keyword)
                    .authorId(authorId)
                    .build();

            SearchResultPaginated searchResult = memoUseCase.searchMemos(dto);
            if (!searchResult.getHits().isEmpty()) {
                return searchResult;
            }
        }

        return new SearchResultPaginated();
    }
}
