package org.sagebionetworks.datawarehouse.dao;

import org.sagebionetworks.datawarehouse.model.WriteRecordResult;

public interface FailedRecordDao {

    void put(WriteRecordResult result);
}
