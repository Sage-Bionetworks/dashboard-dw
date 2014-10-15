package org.sagebionetworks.datawarehouse.dao;

import org.sagebionetworks.datawarehouse.parse.AccessRecord;

public interface AccessRecordDao {

    void put(AccessRecord record);

    void cleanup();

    long count();
}
