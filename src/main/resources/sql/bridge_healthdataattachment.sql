CREATE TABLE IF NOT EXISTS bridge_healthdataattachment_<dateSuffix> (
    id              CHAR(36)        ENCODE RAW          NOT NULL,
    recordId        CHAR(36)        ENCODE RAW          NOT NULL
) INTERLEAVED SORTKEY (
    id,
    recordId
);
