CREATE TABLE IF NOT EXISTS access_record (
    object_id       varchar(12),
    entity_id       bigint,
    elapse_ms       bigint          NOT NULL,
    timestamp       bigint          NOT NULL,
    host            text,
    thread_id       bigint          NOT NULL,
    user_agent      text,
    query           text,
    session_id      char(37)        PRIMARY KEY,
    request_url     text            NOT NULL,
    user_id         integer,
    method          varchar(10)     NOT NULL,
    vm_id           varchar(60),
    instance        integer         NOT NULL,
    response_status    integer      NOT NULL,
    isProd          boolean,
    file_id         char(37)        REFERENCES log_file(id)
);
