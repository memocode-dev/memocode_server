DROP TABLE IF EXISTS tags;

CREATE TABLE tags
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN
);