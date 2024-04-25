DROP TABLE IF EXISTS question_tag;
DROP TABLE IF EXISTS question_comments;
DROP TABLE IF EXISTS questions;

CREATE TABLE questions
(
    id         UUID PRIMARY KEY,
    title      VARCHAR(255),
    content    TEXT,
    user_id    UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    is_deleted BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user_entity (id)
);

CREATE TABLE question_tag
(
    id          UUID PRIMARY KEY,
    question_id UUID,
    tag_id      UUID,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL,
    deleted_at  TIMESTAMP,
    is_deleted  BOOLEAN,
    FOREIGN KEY (question_id) REFERENCES questions (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id),
    UNIQUE (question_id, tag_id)
);

CREATE TABLE question_comments
(
    id                         UUID PRIMARY KEY,
    content                    TEXT,
    user_id                    UUID,
    question_id                UUID,
    parent_question_comment_id UUID,
    created_at                 TIMESTAMP NOT NULL,
    updated_at                 TIMESTAMP NOT NULL,
    deleted_at                 TIMESTAMP,
    is_deleted                 BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user_entity (id),
    FOREIGN KEY (question_id) REFERENCES questions (id),
    FOREIGN KEY (parent_question_comment_id) REFERENCES question_comments (id)
);