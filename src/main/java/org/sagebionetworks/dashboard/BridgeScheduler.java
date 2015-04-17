package org.sagebionetworks.dashboard;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.service.BridgeDynamoDbWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("bridgeScheduler")
public final class BridgeScheduler {

    private final Logger logger = LoggerFactory.getLogger(BridgeScheduler.class);

    @Resource
    private BridgeDynamoDbWorker bridageDynamoDbWorker;

    @Scheduled(cron="00 00 06 * * *", zone="UTC")
    public void copyToDw() {
        logger.info("Begin copying Bridge DynamoDB backups to Redshift.");
        bridageDynamoDbWorker.copyToDw();
        logger.info("Finished copying Bridge DynamoDB backups to Redshift.");
    }
}
