CREATE TABLE memo_tag
(
    id         UUID PRIMARY KEY,
    memo_id    UUID,
    tag_id     UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (memo_id) REFERENCES memos (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id),
    UNIQUE (memo_id, tag_id)
);