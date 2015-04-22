package org.sagebionetworks.dashboard.dao;

import java.util.List;

public interface DwDao {

    /**
     * Creates a data warehouse table.
     */
    void createTable(String createTableQuery);

    /**
     * Deletes the data warehouse table of the specified name.
     */
    void dropTable(String dropTableQuery);

    /**
     * Gets the list of tables whose name has the specified prefix.
     */
    List<String> getTables(String tableNamePrefix);
}
