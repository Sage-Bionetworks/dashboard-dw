CREATE TABLE IF NOT EXISTS bridge_healthdatarecord3_<dateSuffix> (
    id              CHAR(36)        ENCODE LZO          NOT NULL,
    healthCode      CHAR(36)        ENCODE RAW          NOT NULL,
    createdOn       TIMESTAMP       ENCODE DELTA32k     NOT NULL,
    studyId         VARCHAR(50)     ENCODE BYTEDICT     NOT NULL,
    uploadId        CHAR(36)        ENCODE RAW          NOT NULL,
    uploadDate      DATE            ENCODE RAW          NOT NULL,
    schemaId        VARCHAR(250)    ENCODE LZO          NOT NULL,
    schemaRevision  INT             ENCODE DELTA        NOT NULL,
    data            VARCHAR(5000)   ENCODE LZO                  ,
    metaData        VARCHAR(500)    ENCODE LZO                  ,
    version         INT             ENCODE DELTA
) SORTKEY (studyId, uploadId, uploadDate, healthCode);
