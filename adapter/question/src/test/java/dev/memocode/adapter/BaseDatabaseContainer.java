package dev.memocode.adapter;

import org.junit.ClassRule;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class BaseDatabaseContainer {
    public static final String DATABASE_NAME = "testdb";
    public static final String DATABASE_USER = "test";
    public static final String DATABASE_PASSWORD = "test";

    @ClassRule
    public static final Network network = Network.newNetwork();

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", BaseDatabaseContainer::getJdbcUrl);
        registry.add("spring.datasource.password", BaseDatabaseContainer::getPassword);
        registry.add("spring.datasource.username", BaseDatabaseContainer::getUsername);
    }

    public static final PostgreSQLContainer<?> postgresql =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                    .withNetwork(network)
                    .withNetworkAliases("test_postgresql")
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(DATABASE_USER)
                    .withPassword(DATABASE_PASSWORD);

    static {
        postgresql.start();
    }

    public static String getJdbcUrl() {
        return postgresql.getJdbcUrl();
    }

    public static String getUsername() {
        return postgresql.getUsername();
    }

    public static String getPassword() {
        return postgresql.getPassword();
    }
}
