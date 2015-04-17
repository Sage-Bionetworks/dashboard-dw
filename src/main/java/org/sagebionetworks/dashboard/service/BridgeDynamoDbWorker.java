package org.sagebionetworks.dashboard.service;

import org.springframework.stereotype.Service;

@Service("bridgeDynamoDbWorker")
public final class BridgeDynamoDbWorker {

    public void copyToDw() {
        // Create tables for the day
        // 3 threads copy
    }
}
