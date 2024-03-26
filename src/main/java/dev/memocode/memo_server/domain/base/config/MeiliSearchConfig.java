package dev.memocode.memo_server.domain.base.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeiliSearchConfig {

    @Value("${custom.meilisearch.api-key}")
    private String meilisearchApiKey;

    @Value("${custom.meilisearch.url}")
    private String meilisearchUrl;

    @Bean
    public Client meiliSearchClient() {
        return new Client(new Config(meilisearchUrl, meilisearchApiKey));
    }
}
