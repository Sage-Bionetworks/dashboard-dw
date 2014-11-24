CREATE TABLE IF NOT EXISTS access_record AS
    SELECT DISTINCT 
    FROM raw_access_record;

ALTER TABLE access_record
ADD COLUMN entityId BIGINT;
