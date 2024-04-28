DROP TABLE IF EXISTS BATCH_JOB_INSTANCE;
DROP TABLE IF EXISTS BATCH_JOB_EXECUTION;
DROP TABLE IF EXISTS BATCH_JOB_EXECUTION_PARAMS;
DROP TABLE IF EXISTS BATCH_STEP_EXECUTION;
DROP TABLE IF EXISTS BATCH_STEP_EXECUTION_CONTEXT;
DROP TABLE IF EXISTS BATCH_JOB_EXECUTION_CONTEXT;
DROP TABLE IF EXISTS question_tag;
DROP TABLE IF EXISTS question_comments;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS memo_comments;
DROP TABLE IF EXISTS memo_version;
DROP TABLE IF EXISTS memos;
DROP TABLE IF EXISTS tags;
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

CREATE TABLE tags
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE    NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN
);

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
    created_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at             TIMESTAMP WITH TIME ZONE,
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
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE,
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
    created_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at             TIMESTAMP WITH TIME ZONE,
    is_deleted             BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user_entity (id),
    FOREIGN KEY (memo_id) REFERENCES memos (id),
    FOREIGN KEY (parent_memo_comment_id) REFERENCES memo_comments (id)
);



CREATE TABLE questions
(
    id         UUID PRIMARY KEY,
    title      VARCHAR(255),
    content    TEXT,
    user_id    UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user_entity (id)
);

CREATE TABLE question_tag
(
    id          UUID PRIMARY KEY,
    question_id UUID,
    tag_id      UUID,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at  TIMESTAMP WITH TIME ZONE,
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
    created_at                 TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at                 TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at                 TIMESTAMP WITH TIME ZONE,
    is_deleted                 BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user_entity (id),
    FOREIGN KEY (question_id) REFERENCES questions (id),
    FOREIGN KEY (parent_question_comment_id) REFERENCES question_comments (id)
);

CREATE TABLE BATCH_JOB_INSTANCE  (
                                     JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
                                     VERSION BIGINT ,
                                     JOB_NAME VARCHAR(100) NOT NULL,
                                     JOB_KEY VARCHAR(32) NOT NULL,
                                     constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;

CREATE TABLE BATCH_JOB_EXECUTION  (
                                      JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                      VERSION BIGINT  ,
                                      JOB_INSTANCE_ID BIGINT NOT NULL,
                                      CREATE_TIME TIMESTAMP NOT NULL,
                                      START_TIME TIMESTAMP DEFAULT NULL ,
                                      END_TIME TIMESTAMP DEFAULT NULL ,
                                      STATUS VARCHAR(10) ,
                                      EXIT_CODE VARCHAR(2500) ,
                                      EXIT_MESSAGE VARCHAR(2500) ,
                                      LAST_UPDATED TIMESTAMP,
                                      constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
                                          references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
                                             JOB_EXECUTION_ID BIGINT NOT NULL ,
                                             PARAMETER_NAME VARCHAR(100) NOT NULL ,
                                             PARAMETER_TYPE VARCHAR(100) NOT NULL ,
                                             PARAMETER_VALUE VARCHAR(2500) ,
                                             IDENTIFYING CHAR(1) NOT NULL ,
                                             constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
                                                 references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION  (
                                       STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                       VERSION BIGINT NOT NULL,
                                       STEP_NAME VARCHAR(100) NOT NULL,
                                       JOB_EXECUTION_ID BIGINT NOT NULL,
                                       CREATE_TIME TIMESTAMP NOT NULL,
                                       START_TIME TIMESTAMP DEFAULT NULL ,
                                       END_TIME TIMESTAMP DEFAULT NULL ,
                                       STATUS VARCHAR(10) ,
                                       COMMIT_COUNT BIGINT ,
                                       READ_COUNT BIGINT ,
                                       FILTER_COUNT BIGINT ,
                                       WRITE_COUNT BIGINT ,
                                       READ_SKIP_COUNT BIGINT ,
                                       WRITE_SKIP_COUNT BIGINT ,
                                       PROCESS_SKIP_COUNT BIGINT ,
                                       ROLLBACK_COUNT BIGINT ,
                                       EXIT_CODE VARCHAR(2500) ,
                                       EXIT_MESSAGE VARCHAR(2500) ,
                                       LAST_UPDATED TIMESTAMP,
                                       constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
                                           references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
                                               STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                               SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                               SERIALIZED_CONTEXT TEXT ,
                                               constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
                                                   references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
                                              JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                              SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                              SERIALIZED_CONTEXT TEXT ,
                                              constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
                                                  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;