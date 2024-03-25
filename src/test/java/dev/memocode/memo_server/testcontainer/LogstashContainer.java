package dev.memocode.memo_server.testcontainer;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class LogstashContainer extends GenericContainer<LogstashContainer> {

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("docker.elastic.co/logstash/logstash");
    private static final String DEFAULT_IMAGE_TAG = "8.12.2";

    public LogstashContainer() {
        this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_IMAGE_TAG));
    }

    public LogstashContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
    }
}
