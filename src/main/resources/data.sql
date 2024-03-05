DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS selected_memo_version;
DROP TABLE IF EXISTS memo_version;
DROP TABLE IF EXISTS memos;
DROP TABLE IF EXISTS series;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         CHAR(36)            NOT NULL PRIMARY KEY,
    username   VARCHAR(255) UNIQUE NOT NULL,
    nickname   VARCHAR(255)        NOT NULL,
    account_id CHAR(36)            NOT NULL,
    created_at DATETIME            NOT NULL,
    updated_at DATETIME            NOT NULL,
    deleted_at DATETIME,
    is_deleted BOOLEAN             NOT NULL
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE series
(
    id         CHAR(36) PRIMARY KEY,
    title      VARCHAR(255),
    author_id  CHAR(36),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE memos
(
    id             CHAR(36) PRIMARY KEY,
    title          VARCHAR(255),
    content        LONGTEXT,
    author_id      CHAR(36),
    series_id      CHAR(36),
    affinity       INT,
    sequence         INT,
    parent_memo_id CHAR(36),
    created_at     DATETIME NOT NULL,
    updated_at     DATETIME NOT NULL,
    deleted_at     DATETIME,
    is_deleted     BOOLEAN  NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id),
    FOREIGN KEY (series_id) REFERENCES series (id),
    FOREIGN KEY (parent_memo_id) REFERENCES memos (id)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE memo_version
(
    id         CHAR(36) PRIMARY KEY,
    title      VARCHAR(255),
    content    LONGTEXT,
    version    INT,
    memo_id    CHAR(36),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    is_deleted BOOLEAN  NOT NULL,
    FOREIGN KEY (memo_id) REFERENCES memos (id)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE selected_memo_version
(
    id              CHAR(36) PRIMARY KEY,
    memo_id         CHAR(36) UNIQUE,
    memo_version_id CHAR(36) UNIQUE,
    created_at      DATETIME NOT NULL,
    updated_at      DATETIME NOT NULL,
    FOREIGN KEY (memo_id) REFERENCES memos (id),
    FOREIGN KEY (memo_version_id) REFERENCES memo_version (id)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE comments
(
    id                     CHAR(36) PRIMARY KEY,
    content                TEXT,
    author_id              CHAR(36),
    memo_id                CHAR(36),
    parent_post_comment_id CHAR(36),
    created_at             DATETIME NOT NULL,
    updated_at             DATETIME NOT NULL,
    deleted_at             DATETIME,
    is_deleted             BOOLEAN  NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id),
    FOREIGN KEY (memo_id) REFERENCES memos (id),
    FOREIGN KEY (parent_post_comment_id) REFERENCES comments (id)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;