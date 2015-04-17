package org.sagebionetworks.dashboard.service;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeDynamoDao;
import org.springframework.stereotype.Service;

@Service("bridgeDynamoDbWorker")
public final class BridgeDynamoDbWorker {

    @Resource
    private DwConfig dwConfig;

    @Resource
    private BridgeDynamoDao bridgeDynamoDao;

    public void copyToDw() {
        // Get the list of tables
        // Verify that the backups exists
        // Create the dw tables; tag by date
        // Copy over the tables
    }
}
