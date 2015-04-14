package org.sagebionetworks.dashboard.dao;

import java.util.List;

import org.sagebionetworks.dashboard.model.AccessRecord;

public interface AccessRecordDao {

    long count();

    void createTemp();

    void activateTemp();

    List<AccessRecord> nextRecords();
}
