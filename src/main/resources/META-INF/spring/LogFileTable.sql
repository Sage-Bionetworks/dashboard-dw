CREATE TABLE IF NOT EXISTS log_file (
    id          char(37)        UNIQUE,
    file_path   varchar(100)    NOT NULL,
    log_type    integer         NOT NULL,
    status      varchar(10)     NOT NULL,
    PRIMARY KEY (file_path, log_type)
);
