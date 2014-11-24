package org.sagebionetworks.dashboard.dao;

public interface AccessRecordDao {

    void update();

    void vacuum();

    void cleanup();

    long count();
}
