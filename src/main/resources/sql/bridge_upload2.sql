CREATE TABLE IF NOT EXISTS bridge_upload2_<dateSuffix> (
    uploadId                char(36)        not null,
    uploadDate              timestamp       not null,
    filename                varchar(500),
    contentLength           int,
    contentMd5              varchar(30),
    contentType             varchar(30),
    healthCode              char(36)        not null,
    status                  varchar(30),
    validationMessageList   varchar(5000)
);
