package dev.memocode.adapter.adapter_meilisearch_core;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeiliSearchConfig {

    @Value("${custom.meilisearch.api-key}")
    private String meilisearchApiKey;

    @Value("${custom.meilisearch.url}")
    private String meilisearchUrl;

    @Getter
    @Value("${custom.meilisearch.index.memos}")
    private String memoIndexName;

    @Bean
    public Client meiliSearchClient() {
        return new Client(new Config(meilisearchUrl, meilisearchApiKey));
    }
}
