CREATE TABLE IF NOT EXISTS bridge_upload2_<dateSuffix> (
    uploadId        char(36)        encode raw          not null,
    uploadDate      timestamp       encode raw                  ,
    filename        varchar(500)    encode lzo                  ,
    contentLength   int             encode delta32k             ,
    contentMd5      varchar(50)     encode raw                  ,
    contentType     varchar(50)     encode bytedict             ,
    healthCode      char(36)        encode raw          not null,
    status          varchar(50)     encode bytedict
);
