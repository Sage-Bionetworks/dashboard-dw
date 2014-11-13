package org.sagebionetworks.dashboard;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.FailedRecordDao;
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.sagebionetworks.dashboard.service.AccessRecordWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("accessRecordScheduler")
public class AccessRecordScheduler {

    private final Logger logger = LoggerFactory.getLogger(AccessRecordScheduler.class);

    @Resource
    private AccessRecordDao accessRecordDao;

    @Resource
    private FailedRecordDao failedRecordDao;

    @Resource
    private LogFileDao logFileDao;

    @Resource
    private AccessRecordWorker accessRecordWorker;

    /**
     * Initial delay of 0 minutes. Updates every 5 minutes.
     */
    @Scheduled(initialDelay=(0L * 60L * 1000L), fixedRate=(5L * 60L * 1000L))
    public void runRecordWorker() {
        long accessRecords = accessRecordDao.count();
        long failedRecords = failedRecordDao.count();
        long logFiles = logFileDao.count();
        logger.info(accessRecords + " access records, " + failedRecords + " failed records, " + logFiles + " log files.");
        accessRecordWorker.doWork();
    }
}
