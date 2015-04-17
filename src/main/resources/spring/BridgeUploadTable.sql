CREATE TABLE IF NOT EXISTS upload2 (
    uploadId                char(36),
    uploadDate              timestamp,
    filename                vchar(500),
    contentLength           int,
    contentMd5              vchar(30),
    contentType             vchar(30),
    healthCode              char(36),
    status                  vchar(30),
    validationMessageList   vchar(5000),
);
