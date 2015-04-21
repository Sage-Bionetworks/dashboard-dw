package org.sagebionetworks.dashboard.dao;

/**
 * Imports Bridge DynamoDB table backups to Redshift.
 */
public interface BridgeDao {

    /**
     * Creates the data warehouse table of the specified name.
     */
    void create(String tableName);

    /**
     * Deletes the data warehouse table of the specified name.
     */
    void delete(String tableName);

    /**
     * Copies the S3 backup file to the specified data warehouse table.
     *
     * @param s3Path  The S3 backup file
     * @param dwTable  The data warehouse table to copy to
     */
    void copy(String s3Path, String dwTable);
}
