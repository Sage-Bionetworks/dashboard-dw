CREATE TABLE IF NOT EXISTS access_log_file (
    id          varchar(100)    PRIMARY KEY,
    file_path   varchar(100)    NOT NULL
);

CREATE INDEX ON access_log_file USING btree(id);
