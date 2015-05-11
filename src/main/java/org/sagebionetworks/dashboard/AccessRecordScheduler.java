package org.sagebionetworks.dashboard;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.sagebionetworks.dashboard.dao.RawAccessRecordDao;
import org.sagebionetworks.dashboard.service.AccessRecordWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("accessRecordScheduler")
public class AccessRecordScheduler {

    private final Logger logger = LoggerFactory.getLogger(AccessRecordScheduler.class);

    @Resource
    private RawAccessRecordDao rawAccessRecordDao;

    @Resource
    private LogFileDao logFileDao;

    @Resource
    private AccessRecordWorker accessRecordWorker;

    @Resource
    private AccessRecordDao accessRecordDao;

    /**
     * Copy the access record log files in S3 buckets to raw_access_record.
     * Initial delay of 30 seconds. Updates every 10 minutes.
     */
    @Scheduled(initialDelay=(30L * 1000L), fixedRate=(600L * 1000L))
    public void runRawRecordWorker() {
        logger.info(rawAccessRecordDao.count() + " raw access records, " +
                logFileDao.count() + " log files.");
        accessRecordWorker.copy();
    }

    /**
     * Update the access_record table.
     * Initial delay of 1 minutes. Updates every 1 hour.
     */
    @Scheduled(initialDelay=(60L * 1000L), fixedRate=(60L * 60L * 1000L))
    public void runRecordWorker() {
        logger.info(accessRecordDao.count() + " access records.");
        accessRecordWorker.update();
    }
}
