CREATE TABLE IF NOT EXISTS bridge_upload2_<dateSuffix> (
    uploadId        CHAR(36)        ENCODE RAW          NOT NULL,
    uploadDate      DATE            ENCODE RAW                  ,
    filename        VARCHAR(500)    ENCODE LZO                  ,
    contentLength   INT             ENCODE DELTA32K             ,
    contentMd5      VARCHAR(50)     ENCODE RAW                  ,
    contentType     VARCHAR(50)     ENCODE BYTEDICT             ,
    healthCode      CHAR(36)        ENCODE RAW          NOT NULL,
    status          VARCHAR(50)     ENCODE BYTEDICT
) SORTKEY (uploadDate, healthCode);
