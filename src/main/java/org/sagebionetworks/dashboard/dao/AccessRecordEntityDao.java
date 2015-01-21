package org.sagebionetworks.dashboard.dao;

import java.util.Map;

public interface AccessRecordEntityDao {

    void insertNewRecords(Map<String,?>[] batchValues);
    long count();
    void vacuum();
}
