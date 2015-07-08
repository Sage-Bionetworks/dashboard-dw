CREATE TABLE IF NOT EXISTS bridge_healthdatarecord3_<dateSuffix> (
    id                  CHAR(36)        ENCODE RAW          NOT NULL,
    healthCode          CHAR(36)        ENCODE RAW          NOT NULL,
    createdOn           BIGINT          ENCODE DELTA32k             ,
    studyId             VARCHAR(50)     ENCODE BYTEDICT             ,
    uploadId            CHAR(36)        ENCODE RAW          NOT NULL,
    uploadDate          DATE            ENCODE RAW                  ,
    schemaId            VARCHAR(250)    ENCODE LZO                  ,
    schemaRevision      INT             ENCODE DELTA                ,
    data                VARCHAR(10000)  ENCODE LZO                  ,
    metaData            VARCHAR(5000)   ENCODE LZO                  ,
    userSharingScope    VARCHAR(500)    ENCODE BYTEDICT             ,
    version             INT             ENCODE DELTA
) SORTKEY (
    studyId,
    uploadDate,
    schemaId,
    healthCode,
    uploadId
);
