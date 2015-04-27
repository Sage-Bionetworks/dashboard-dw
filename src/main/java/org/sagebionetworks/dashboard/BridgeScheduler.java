package org.sagebionetworks.dashboard;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.service.BridgeDynamoBackupImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Schedules the work of importing Bridge data into data warehouse.
 */
@Component("bridgeScheduler")
public final class BridgeScheduler {

    private final Logger logger = LoggerFactory.getLogger(BridgeScheduler.class);

    @Resource
    private BridgeDynamoBackupImporter bridgeDynamoBackupImporter;

    /**
     * 12 pm UTC or 5 am PDT.  Make sure this happens after daily backup is done.
     */
    @Scheduled(cron="00 00 12 * * *", zone="UTC")
    public void doWork() {
        logger.info("Begin importing Bridge DynamoDB backups to Redshift.");
        bridgeDynamoBackupImporter.doWork();
        logger.info("Finished importing Bridge DynamoDB backups to Redshift.");
    }
}