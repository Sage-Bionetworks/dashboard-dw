package org.sagebionetworks.dashboard.dao;

/**
 * Imports Bridge DynamoDB table backups to Redshift.
 */
public interface BridgeDynamoBackupImportDao {

    /**
     * Creates the data warehouse table of the specified name.
     * The actual table name is suffixed by the date string.
     *
     * @return The full name of the table created
     */
    String createTable(String tableName, String dateSuffix);

    /**
     * Deletes the data warehouse table of the specified name.
     *
     * @return The full name of the table deleted
     */
    String dropTable(String tableName, String dateSuffix);

    /**
     * Copies the S3 backup file to the specified data warehouse table.
     *
     * @param s3Path  The S3 backup file
     * @param fullTableName  The name of the data warehouse table to copy to
     */
    void copy(String s3Path, String fullTableName);
}
