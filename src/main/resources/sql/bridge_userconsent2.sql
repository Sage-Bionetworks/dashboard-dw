CREATE TABLE IF NOT EXISTS bridge_userconsent2_<dateSuffix> (
    healthCodeStudy VARCHAR(100)    ENCODE LZO          NOT NULL,
    healthCode      CHAR(36)        ENCODE RAW          NOT NULL,
    studyKey        VARCHAR(50)     ENCODE BYTEDICT     NOT NULL,
    signedOn        BIGINT          ENCODE DELTA32K     NOT NULL
) SORTKEY (studyKey, signedOn);
