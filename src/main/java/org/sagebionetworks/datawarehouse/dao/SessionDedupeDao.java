package org.sagebionetworks.datawarehouse.dao;

public interface SessionDedupeDao {

    /**
     * Whether the session has already been processed.
     */
    boolean isProcessed(String sessionId);
}
