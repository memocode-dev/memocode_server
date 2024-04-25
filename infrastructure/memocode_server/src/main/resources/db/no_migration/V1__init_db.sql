DROP TABLE IF EXISTS memo_comments;
DROP TABLE IF EXISTS memo_version;
DROP TABLE IF EXISTS memos;
DROP TABLE IF EXISTS question_tag;
DROP TABLE IF EXISTS question_comments;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS user_entity;

CREATE TABLE user_entity
(
    id                          UUID NOT NULL,
    email                       VARCHAR(255) NULL,
    email_constraint            VARCHAR(255) NULL,
    email_verified              BOOL NOT NULL DEFAULT false,
    enabled                     BOOL NOT NULL DEFAULT false,
    federation_link             VARCHAR(255) NULL,
    first_name                  VARCHAR(255) NULL,
    last_name                   VARCHAR(255) NULL,
    realm_id                    VARCHAR(255) NULL,
    username                    VARCHAR(255) NULL,
    created_timestamp           int8 NULL,
    service_account_client_link varchar(255) NULL,
    not_before                  int4 NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE (realm_id, email_constraint),
    UNIQUE (realm_id, username)
);

CREATE INDEX idx_user_email ON user_entity USING btree (email);
CREATE INDEX idx_user_service_account ON user_entity USING btree (realm_id, service_account_client_link);

CREATE TABLE memos
(
    id                     UUID PRIMARY KEY,
    title                  VARCHAR(255),
    content                TEXT,
    summary                TEXT,
    author_id              UUID,
    visibility             BOOLEAN,
    visibility_achieved_at TIMESTAMP,
    bookmarked             BOOLEAN,
    security               BOOLEAN,
    created_at             TIMESTAMP NOT NULL,
    updated_at             TIMESTAMP NOT NULL,
    deleted_at             TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES user_entity (id)
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
    FOREIGN KEY (memo_id) REFERENCES memos (id)
);

CREATE TABLE memo_comments
(
    id                     UUID PRIMARY KEY,
    content                TEXT,
    author_id              UUID,
    memo_id                UUID,
    parent_memo_comment_id UUID,
    created_at             TIMESTAMP NOT NULL,
    updated_at             TIMESTAMP NOT NULL,
    deleted_at             TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES user_entity (id),
    FOREIGN KEY (memo_id) REFERENCES memos (id),
    FOREIGN KEY (parent_memo_comment_id) REFERENCES memo_comments (id)
);

CREATE TABLE questions
(
    id         UUID PRIMARY KEY,
    title      VARCHAR(255),
    content    TEXT,
    author_id  UUID,
    affinity   INT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES user_entity (id)
);

CREATE TABLE tags
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    deleted_at TIMESTAMP
);

CREATE TABLE question_tag
(
    id          UUID PRIMARY KEY,
    question_id UUID,
    tag_id      UUID,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL,
    deleted_at  TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE TABLE question_comments
(
    id                         UUID PRIMARY KEY,
    content                    TEXT,
    author_id                  UUID,
    question_id                UUID,
    parent_question_comment_id UUID,
    created_at                 TIMESTAMP NOT NULL,
    updated_at                 TIMESTAMP NOT NULL,
    deleted_at                 TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES user_entity (id),
    FOREIGN KEY (question_id) REFERENCES questions (id),
    FOREIGN KEY (parent_question_comment_id) REFERENCES question_comments (id)
);