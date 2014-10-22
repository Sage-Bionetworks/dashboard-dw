CREATE TABLE IF NOT EXISTS log_file (
    id          varchar(100)    PRIMARY KEY,
    file_path   varchar(100)    NOT NULL,
    log_type    integer         NOT NULL
);

CREATE INDEX ON log_file USING btree(id);

CREATE INDEX ON log_file USING btree(log_type);
