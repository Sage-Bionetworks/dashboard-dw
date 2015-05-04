package org.sagebionetworks.dashboard.dao;

/**
 * Imports Bridge data to data warehouse.
 */
public interface BridgeImportDao {

    /**
     * Creates the data warehouse table.
     */
    void createTable(String createTableQuery);

    /**
     * Deletes the data warehouse table.
     */
    void dropTable(String fullDwTableName);

    /**
     * Copies DynamoDB to the specified data warehouse table.
     *
     * @param fullDynamoTableName  Fully qualified DynamoDB table name
     * @param fullDwTableName  Fully qualified data warehouse table name
     */
    void copyFromDynamo(String fullDynamoTableName, String fullDwTableName);

    /**
     * Updates the table view to point to the specified snapshot.
     * Creates the view if the view does not already exist.
     */
    void updateView(String viewName, String fullDwTableName);
}
