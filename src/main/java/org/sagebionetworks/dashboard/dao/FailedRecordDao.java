package org.sagebionetworks.dashboard.dao;

public interface FailedRecordDao {

    void put(String fileId, int lineNumber, String sessionId);
    void cleanup();
    long count();
}
