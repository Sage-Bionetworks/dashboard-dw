package org.sagebionetworks.dashboard.dao;


public interface AccessRecordDao {

    long count();

    void createTemp();

    void activateTemp();
}
