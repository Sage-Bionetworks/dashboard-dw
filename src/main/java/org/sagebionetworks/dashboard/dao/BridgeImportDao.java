package org.sagebionetworks.dashboard.dao;

/**
 * Imports Bridge data to data warehouse.
 */
public interface BridgeImportDao {

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
     * Copies DynamoDB to the specified data warehouse table.
     *
     * @param dynamoTable  The DynamoDB table
     * @param dwFullTableName  The name of the data warehouse table to copy to
     */
    void copyFromDynamo(String dynamoTable, String dwFullTableName);

    /**
     * Updates the table view to point to the specified snapshot.
     * Creates the view if the view does not already exist.
     */
    void updateView(String tableName, String snapshotName);
}
