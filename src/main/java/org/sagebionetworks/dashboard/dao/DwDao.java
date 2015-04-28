package org.sagebionetworks.dashboard.dao;

import java.util.List;

public interface DwDao {

    /**
     * Executes a data warehouse query.
     */
    void execute(String query);

    /**
     * Gets the list of tables whose name has the specified prefix.
     */
    List<String> getTables(String tableNamePrefix);
}
