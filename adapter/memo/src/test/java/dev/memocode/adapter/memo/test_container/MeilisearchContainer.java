package dev.memocode.adapter.memo.test_container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class MeilisearchContainer extends GenericContainer<MeilisearchContainer> {

    private static final int MEILISEARCH_DEFAULT_PORT = 7700;
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("getmeili/meilisearch");
    private static final String DEFAULT_IMAGE_TAG = "v1.7";

    public MeilisearchContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_IMAGE_TAG));
    }

    public MeilisearchContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        this.addExposedPort(MEILISEARCH_DEFAULT_PORT);
    }

    public MeilisearchContainer withMasterKey(String masterKey) {
        withEnv("MEILI_MASTER_KEY", masterKey);
        return self();
    }
}
