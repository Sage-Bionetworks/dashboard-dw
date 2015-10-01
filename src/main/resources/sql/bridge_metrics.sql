CREATE TABLE IF NOT EXISTS bridge_metrics (
    request_id      CHAR(36)        ENCODE RAW          NOT NULL,
    request_start   TIMESTAMP       ENCODE DELTA32K     NOT NULL,
    request_end     TIMESTAMP       ENCODE DELTA32K     NOT NULL,
    remote_address  VARCHAR(100)    ENCODE LZO          NOT NULL,
    method          VARCHAR(10)     ENCODE BYTEDICT     NOT NULL,
    uri             VARCHAR(250)    ENCODE LZO          NOT NULL,
    status          INT             ENCODE DELTA        NOT NULL,
    protocol        VARCHAR(15)     ENCODE BYTEDICT     NOT NULL,
    user_agent      VARCHAR(500)    ENCODE LZO                  ,
    session_id      CHAR(36)        ENCODE RAW                  ,
    user_id         VARCHAR(25)     ENCODE RAW                  ,
    study           VARCHAR(50)     ENCODE BYTEDICT             ,
    sharing_option  VARCHAR(100)    ENCODE BYTEDICT             ,
    upload_id       CHAR(36)        ENCODE RAW                  ,
    upload_size     INT             ENCODE DELTA32K
) INTERLEAVED SORTKEY (
    request_id,
    session_id,
    request_start,
    uri,
    user_id,
    study,
    sharing_option,
    upload_id
);
