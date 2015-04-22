package org.sagebionetworks.dashboard.dao;

public interface DwDao {

    /**
     * Creates a data warehouse table.
     */
    void createTable(String createTableQuery);

    /**
     * Deletes the data warehouse table of the specified name.
     */
    void dropTable(String dropTableQuery);
}
