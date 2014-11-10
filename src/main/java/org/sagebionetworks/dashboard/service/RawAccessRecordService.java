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

    private final Logger logger = LoggerFactory.getLogger(RawAccessRecordService.class);

    @Resource
    private LogFileDao logFileDao;

    @Resource
    private RawAccessRecordDao rawAccessRecordDao;

    public void update(final String filePath, final String username, final String password) {
        if (logFileDao.isCompleted(filePath)) {
            return;
        }
        String id = UUID.randomUUID().toString();
        try {
            // type 0 for access_record
            logFileDao.put(filePath, id, 0);
            rawAccessRecordDao.copy(filePath, username, password);
            logFileDao.update(id);
        } catch (Throwable exception) {
            // if it's not a duplicate file, log the error
            if (!exception.getMessage().contains("already exists")) {
                logger.error("Failed to copy file " + filePath);
            }
            return;
        }
    }
}
