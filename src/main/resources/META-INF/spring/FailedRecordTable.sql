CREATE TABLE IF NOT EXISTS failed_record (
    file_id         char(37)        REFERENCES log_file(id),
    line_number     integer         NOT NULL,
    session_id      char(37)
);

CREATE INDEX ON failed_record USING btree(file_id);

CREATE INDEX ON failed_record USING btree(session_id);
