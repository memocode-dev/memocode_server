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