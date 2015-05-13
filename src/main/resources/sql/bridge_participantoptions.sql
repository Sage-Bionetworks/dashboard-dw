CREATE TABLE IF NOT EXISTS bridge_participantoptions_<dateSuffix> (
    healthDataCode      CHAR(36)        ENCODE RAW          NOT NULL,
    studyKey            VARCHAR(50)     ENCODE BYTEDICT     NOT NULL,
    data                VARCHAR(500)    ENCODE BYTEDICT     NOT NULL
) SORTKEY (studyKey, data);
