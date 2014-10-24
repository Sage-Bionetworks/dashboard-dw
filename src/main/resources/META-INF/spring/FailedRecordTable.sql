CREATE TABLE IF NOT EXISTS failed_record (
    file_id         varchar(100)    REFERENCES log_file(id),
    line_number     integer         NOT NULL,
    session_id      varchar(100)
);

CREATE INDEX ON failed_record USING btree(file_id);

CREATE INDEX ON failed_record USING btree(session_id);
