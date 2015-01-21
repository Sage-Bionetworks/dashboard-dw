package org.sagebionetworks.dashboard.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.sagebionetworks.dashboard.dao.RawAccessRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("rawAccessRecordService")
public class RawAccessRecordService {

    private final String PREFIX = "s3://";

    private final Logger logger = LoggerFactory.getLogger(RawAccessRecordService.class);

    @Resource
    private LogFileDao logFileDao;

    @Resource
    private RawAccessRecordDao rawAccessRecordDao;

    public void update(final String bucket, final String filePath, final String username, final String password) {
        if (logFileDao.exist(filePath)) {
            return;
        }
        String id = UUID.randomUUID().toString();
        try {
            // type 0 for access_record
            logFileDao.put(filePath, id, 0);
            try {
                rawAccessRecordDao.copy(PREFIX + bucket + "/" + filePath, username, password);
                logger.info("Finish adding " + filePath + " into raw_access_record table.");
                logFileDao.update(id);
            } catch (Throwable e) {
                logFileDao.updateFailed(id);
            }
        } catch (Throwable exception) {
            logger.error("Failed to copy file " + filePath, exception);
        }
    }

    public void vacuum() {
        rawAccessRecordDao.vacuum();
    }
}
