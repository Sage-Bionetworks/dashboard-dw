CREATE TABLE IF NOT EXISTS bridge_upload2_<dateSuffix> (
    uploadId                char(36)        not null,
    uploadDate              timestamp,
    filename                varchar(500),
    contentLength           int,
    contentMd5              varchar(50),
    contentType             varchar(50),
    healthCode              char(36)        not null,
    status                  varchar(50)
);
