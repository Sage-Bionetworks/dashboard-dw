package org.sagebionetworks.datawarehouse.service;

import org.sagebionetworks.datawarehouse.model.WriteRecordResult;

public interface UpdateRecordCallback {

    void handle(WriteRecordResult result);
}
