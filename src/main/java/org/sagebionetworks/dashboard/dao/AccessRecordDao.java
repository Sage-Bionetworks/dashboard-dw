package org.sagebionetworks.dashboard.dao;

import java.util.List;

import org.sagebionetworks.dashboard.parse.AccessRecord;

public interface AccessRecordDao {

    void vacuum();

    void cleanup();

    long count();

    void insertNewRecords();

    List<AccessRecord> nextRecords();
}
