CREATE TABLE IF NOT EXISTS log_file (
    id          char(37)     NOT NULL,
    file_path   varchar(100)    NOT NULL,
    log_type    integer         NOT NULL,
    PRIMARY KEY (file_path, log_type)
);

CREATE INDEX ON log_file USING btree(id);
