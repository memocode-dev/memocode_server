DROP TABLE IF EXISTS memo_comments;
DROP TABLE IF EXISTS memo_version;
DROP TABLE IF EXISTS memos;

CREATE TABLE memos
(
    id                     UUID PRIMARY KEY,
    title                  VARCHAR(255),
    content                TEXT,
    summary                TEXT,
    user_id                UUID,
    visibility             BOOLEAN,
    visibility_achieved_at TIMESTAMP,
    bookmarked             BOOLEAN,
    security               BOOLEAN,
    created_at             TIMESTAMP NOT NULL,
    updated_at             TIMESTAMP NOT NULL,
    deleted_at             TIMESTAMP,
    is_deleted             BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user_entity (id)
);

CREATE TABLE memo_version
(
    id         UUID PRIMARY KEY,
    title      VARCHAR(255),
    content    TEXT,
    version    INT,
    memo_id    UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN,
    FOREIGN KEY (memo_id) REFERENCES memos (id)
);

CREATE TABLE memo_comments
(
    id                     UUID PRIMARY KEY,
    content                TEXT,
    user_id                UUID,
    memo_id                UUID,
    parent_memo_comment_id UUID,
    created_at             TIMESTAMP NOT NULL,
    updated_at             TIMESTAMP NOT NULL,
    deleted_at             TIMESTAMP,
    is_deleted             BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user_entity (id),
    FOREIGN KEY (memo_id) REFERENCES memos (id),
    FOREIGN KEY (parent_memo_comment_id) REFERENCES memo_comments (id)
);