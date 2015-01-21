package org.sagebionetworks.dashboard;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.AccessRecordEntityDao;
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

    @Resource
    private AccessRecordEntityDao accessRecordEntityDao;

    /**
     * Copy the access record log files in S3 buckets to raw_access_record.
     * Initial delay of 0 minutes. Updates every 1.5 minutes.
     */
    @Scheduled(initialDelay=(0L * 60L * 1000L), fixedRate=(90L * 1000L))
    public void runRawRecordWorker() {
        logger.info(rawAccessRecordDao.count() + " raw access records, " +
                logFileDao.count() + " log files.");
        accessRecordWorker.copy();
    }

    /**
     * Update the access_record table.
     * Initial delay of 1.5 minutes. Updates every 13 minutes.
     */
    @Scheduled(initialDelay=(90L * 1000L), fixedRate=(13L * 60L * 1000L))
    public void runRecordWorker() {
        logger.info(accessRecordDao.count() + " access records.");
        accessRecordWorker.update();
    }

    /**
     * Update the access_record_entity table.
     * Initial delay of 3 minutes. Updates every 7 minutes.
     */
    @Scheduled(initialDelay=(3L * 60L * 1000L), fixedRate=(7L * 60L * 1000L))
    public void updateEntityLookupTable() {
        logger.info(accessRecordEntityDao.count() + " entity lookup records.");
        accessRecordWorker.updateEntityLookupTable();
    }

    /**
     * Clean up the access_record table.
     * Initial delay of 30 minutes. Updates every 60 minutes.
     */
    @Scheduled(initialDelay=(30L * 60L * 1000L), fixedRate=(60L * 60L * 1000L))
    public void vacuumAccessRecord() {
        accessRecordWorker.vacuumAccessRecord();
    }

    /**
     * Clean up the raw_access_record table.
     * Initial delay of 30 minutes. Updates every 60 minutes.
     */
    @Scheduled(initialDelay=(30L * 60L * 1000L), fixedRate=(60L * 60L * 1000L))
    public void vacuumRawAccessRecord() {
        accessRecordWorker.vacuumRawAccessRecord();
    }

    /**
     * Clean up the access_record_entity table.
     * Initial delay of 30 minutes. Updates every 60 minutes.
     */
    @Scheduled(initialDelay=(30L * 60L * 1000L), fixedRate=(60L * 60L * 1000L))
    public void vacuumEntityLookupTable() {
        accessRecordWorker.vacuumEntityLookupTable();
    }

    /**
     * Clean up the access_record_entity table.
     * Initial delay of 30 minutes. Updates every 60 minutes.
     */
    @Scheduled(initialDelay=(30L * 60L * 1000L), fixedRate=(60L * 60L * 1000L))
    public void vacuumLogFile() {
        accessRecordWorker.vacuumLogFile();
    }
}
