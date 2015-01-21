package org.sagebionetworks.dashboard.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("accessRecordEntityDao")
public interface AccessRecordEntityDao {

    void insertNewRecords(Map<String,?>[] batchValues);
    long count();
}
