package org.sagebionetworks.dashboard.dao;


public interface RawAccessRecordDao {

    void copy(String filePath, String username, String password);
    void vacuum();
    long count();
}
